package com.memo.memo.topic_content.service

import com.memo.memo.common.exception.InvalidInputException
import com.memo.memo.content.dto.ContentDtoResponse
import com.memo.memo.content.entity.Content
import com.memo.memo.member.entity.Member
import com.memo.memo.member.repository.MemberRepository
import com.memo.memo.topic_content.dto.TopicContentRequestDto
import com.memo.memo.topic_content.dto.TopicContentResponseDto
import com.memo.memo.topic_content.dto.TopicDto
import com.memo.memo.topic_content.dto.TopicResponseDto
import com.memo.memo.topic_content.entity.TopicContent
import com.memo.memo.topic_content.repository.TopicContentRepository
import com.memo.memo.topic_content.repository.TopicRepository
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
    fun createTopicContent(userId: Long, topicContentRequestDto:TopicContentRequestDto): TopicDto {
        val findMember = memberRepository.findByIdOrNull(userId)
            ?: throw InvalidInputException("존재하지 않는 회원입니다.")
        val findTopic = topicRepository.findByIdOrNull(topicContentRequestDto.topicId)
            ?: throw InvalidInputException("존재하지 않는 주제입니다.")

        val topicContent = topicContentRequestDto.toEntity(findMember, findTopic)
        topicContentRepository.save(topicContent)
        return TopicDto(topicId = findTopic.id!!, topicName = findTopic.name!!)
    }

    // TopicContent 수정
    @Transactional
    fun updateTopicContent(userId: Long, topicContentRequestDto:TopicContentRequestDto): TopicDto {
        val topicContent = topicContentRepository.findByIdOrNull(topicContentRequestDto.contentId)
            ?: throw IllegalArgumentException("작성한 기록을 찾을 수 없습니다.")
        val findTopic = topicRepository.findByIdOrNull(topicContentRequestDto.topicId)
            ?: throw InvalidInputException("존재하지 않는 주제입니다.")
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
    fun getAllTopicContentsByMemberAndTopic(userId: Long, topicId: Long, page: Int, size: Int): Page<TopicContentResponseDto> {
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
        size: Int
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
        val getTopicContent = topicContentRepository.findByIdOrNull(contentId)
            ?: throw IllegalArgumentException("작성한 기록을 찾을 수 없습니다.")
        return TopicContentResponseDto(
            contentId = getTopicContent.id,
            title = getTopicContent.title,
            content = getTopicContent.content,
            date = getTopicContent.createdDate
        )
    }
}