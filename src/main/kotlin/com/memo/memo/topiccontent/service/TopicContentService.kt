package com.memo.memo.topiccontent.service

import com.memo.memo.common.exception.exceptions.TopicContentNotFoundException
import com.memo.memo.common.exception.exceptions.TopicNotFoundException
import com.memo.memo.common.exception.exceptions.UserNotFoundException
import com.memo.memo.member.repository.MemberRepository
import com.memo.memo.topiccontent.dto.TopicContentRequestDto
import com.memo.memo.topiccontent.dto.TopicContentResponseDto
import com.memo.memo.topiccontent.dto.TopicDto
import com.memo.memo.topiccontent.entity.TopicContent
import com.memo.memo.topiccontent.repository.TopicContentRepository
import com.memo.memo.topiccontent.repository.TopicRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TopicContentService(
    private val topicRepository: TopicRepository,
    private val topicContentRepository: TopicContentRepository,
    private val memberRepository: MemberRepository,
) {
    // TopicContent 생성
    @Transactional
    fun createTopicContent(
        userId: Long,
        topicContentRequestDto: TopicContentRequestDto,
    ): TopicDto {
        val findMember =
            memberRepository.findByIdOrNull(userId)
                ?: throw UserNotFoundException()
        val findTopic =
            topicRepository.findByIdOrNull(topicContentRequestDto.topicId)
                ?: throw TopicNotFoundException()

        val topicContent = topicContentRequestDto.toEntity(findMember, findTopic)
        topicContentRepository.save(topicContent)
        return TopicDto(topicId = findTopic.id!!, topicName = findTopic.name!!)
    }

    // TopicContent 수정
    @Transactional
    fun updateTopicContent(
        userId: Long,
        topicContentRequestDto: TopicContentRequestDto,
    ): TopicDto {
        val topicContent =
            topicContentRepository.findByIdOrNull(topicContentRequestDto.contentId)
                ?: throw TopicContentNotFoundException()
        val findTopic =
            topicRepository.findByIdOrNull(topicContentRequestDto.topicId)
                ?: throw TopicNotFoundException()
        topicContent.title = topicContentRequestDto.title
        topicContent.content = topicContentRequestDto.content
        topicContentRepository.save(topicContent)
        return TopicDto(topicId = findTopic.id!!, topicName = findTopic.name!!)
    }

    // TopicContent 삭제
    @Transactional
    fun deleteTopicContent(id: Long): String {
        topicContentRepository.deleteById(id)
        return "삭제 완료되었습니다."
    }

    // 특정 멤버와 Topic에 해당하는 모든 TopicContent 조회
    @Transactional(readOnly = true)
    fun getAllTopicContentsByMemberAndTopic(
        userId: Long,
        topicId: Long,
        page: Int,
        size: Int,
    ): Page<TopicContentResponseDto> {
        // PageRequest를 사용하여 페이지와 크기를 설정합니다.
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("createdDate").descending())
        val topicContent: Page<TopicContent> = topicContentRepository.findByMemberIdAndTopicId(userId, topicId, pageable)

        // topicContent TopicContentResponseDto 매핑하여 반환
        return topicContent.map { content ->
            TopicContentResponseDto(
                contentId = content.id,
                title = content.title,
                content = content.content,
                date = content.createdDate,
            )
        }
    }

    fun searchTopicContentByKeyword(
        userId: Long,
        topicId: Long,
        keyword: String,
        page: Int,
        size: Int,
    ): Page<TopicContentResponseDto>? {
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("createdDate").descending())
        val topicContent = topicContentRepository.searchTopicContentByMemberAndKeyword(userId, topicId, keyword, pageable)

        // TopicContentResponseDto 형태로 변환하여 반환
        return topicContent.map { content ->
            TopicContentResponseDto(
                contentId = content.id,
                title = content.title,
                content = content.content,
                date = content.createdDate,
            )
        }
    }

    fun getTopicContent(contentId: Long): TopicContentResponseDto {
        val getTopicContent =
            topicContentRepository.findByIdOrNull(contentId)
                ?: throw TopicContentNotFoundException()
        return TopicContentResponseDto(
            contentId = getTopicContent.id,
            title = getTopicContent.title,
            content = getTopicContent.content,
            date = getTopicContent.createdDate,
        )
    }
}
