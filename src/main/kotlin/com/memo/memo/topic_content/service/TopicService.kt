package com.memo.memo.topic_content.service

import com.memo.memo.common.exception.exceptions.TopicNotFoundException
import com.memo.memo.common.exception.exceptions.UserNotFoundException
import com.memo.memo.member.repository.MemberRepository
import com.memo.memo.topic_content.dto.TopicDto
import com.memo.memo.topic_content.dto.TopicResponseDto
import com.memo.memo.topic_content.entity.Topic
import com.memo.memo.topic_content.repository.TopicContentRepository
import com.memo.memo.topic_content.repository.TopicRepository
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Slf4j
class TopicService(
    private val topicRepository: TopicRepository,
    private val topicContentRepository: TopicContentRepository,
    private val memberRepository: MemberRepository,
) {
    @Transactional
    fun createTopic(
        userId: Long,
        topicName: String,
    ): Long? {
        val findMember =
            memberRepository.findByIdOrNull(userId)
                ?: throw UserNotFoundException()

        val topic = Topic(name = topicName, member = findMember)
        topicRepository.save(topic)

        return topic.id
    }

    // Topic 수정
    @Transactional
    fun updateTopic(
        userId: Long,
        topicRequestDto: TopicDto,
    ): String {
        val findTopic = topicRepository.findByIdOrNull(topicRequestDto.topicId)

        return if (findTopic != null) {
            // 수정할 주제가 존재할 경우
            findTopic.name = topicRequestDto.topicName
            topicRepository.save(findTopic) // 변경된 내용 저장
            return "수정이 완료되었습니다."
        } else {
            throw TopicNotFoundException()
        }
    }

    // Topic 삭제
    @Transactional
    fun deleteTopic(topidId: Long): String {
        log.info("topic 삭제 시작")
        topicRepository.deleteById(topidId)
        return "주제가 정상적으로 삭제되었습니다."
    }

    // 특정 멤버가 작성한 모든 Topic과 각 Topic에 속한 메모 개수 조회
    @Transactional(readOnly = true)
    fun getTopicsWithContentCountByMember(memberId: Long): List<TopicResponseDto> {
        val findMember =
            memberRepository.findByIdOrNull(memberId)
                ?: throw UserNotFoundException()
        return topicRepository.findByMemberId(memberId).map { topic ->
            TopicResponseDto(
                topicId = topic.id,
                topicName = topic.name,
                contentNum = topicContentRepository.countByMemberAndTopic(findMember, topic),
            )
        }
    }
}
