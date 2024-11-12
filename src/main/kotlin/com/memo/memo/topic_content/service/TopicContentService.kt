package com.memo.memo.topic_content.service

import com.memo.memo.common.exception.InvalidInputException
import com.memo.memo.member.repository.MemberRepository
import com.memo.memo.topic_content.dto.TopicContentRequestDto
import com.memo.memo.topic_content.dto.TopicContentResponseDto
import com.memo.memo.topic_content.repository.TopicContentRepository
import com.memo.memo.topic_content.repository.TopicRepository
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
    fun createTopicContent(userId: Long, topicContentRequestDto:TopicContentRequestDto): String {
        val findMember = memberRepository.findByIdOrNull(userId)
            ?: throw InvalidInputException("존재하지 않는 회원입니다.")
        val findTopic = topicRepository.findByIdOrNull(topicContentRequestDto.topicId)
            ?: throw InvalidInputException("존재하지 않는 주제입니다.")

        val topicContent = topicContentRequestDto.toEntity(findMember, findTopic)
        topicContentRepository.save(topicContent)
        return "정상적으로 저장했습니다."
    }

    // TopicContent 수정
    @Transactional
    fun updateTopicContent(userId: Long, topicContentRequestDto:TopicContentRequestDto): String {
        val topicContent = topicContentRepository.findByIdOrNull(topicContentRequestDto.contentId)
            ?: throw IllegalArgumentException("작성한 기록을 찾을 수 없습니다.")
        topicContent.title = topicContentRequestDto.title
        topicContent.content = topicContentRequestDto.content
        topicContentRepository.save(topicContent)
        return "정상적으로 수정했습니다."
    }

    // TopicContent 삭제
    @Transactional
    fun deleteTopicContent(id: Long): String {
        topicContentRepository.deleteById(id)
        return "삭제 완료되었습니다."
    }

    // 특정 멤버와 Topic에 해당하는 모든 TopicContent 조회
    @Transactional(readOnly = true)
    fun getAllTopicContentsByMemberAndTopic(memberId: Long, topicId: Long): List<TopicContentResponseDto> {
        return topicContentRepository.findByMemberIdAndTopicId(memberId, topicId)
            .map { TopicContentResponseDto(title = it.title, content = it.content) }
    }
}