package com.memo.memo.topic_content.service

import com.memo.memo.common.exception.InvalidInputException
import com.memo.memo.member.repository.MemberRepository
import com.memo.memo.topic_content.dto.TopicRequestDto
import com.memo.memo.topic_content.dto.TopicResponseDto
import com.memo.memo.topic_content.entity.Topic
import com.memo.memo.topic_content.repository.TopicContentRepository
import com.memo.memo.topic_content.repository.TopicRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TopicService(
    private val topicRepository: TopicRepository,
    private val topicContentRepository: TopicContentRepository,
    private val memberRepository: MemberRepository
) {
    @Transactional
    fun createTopic(userId: Long, topicName: String): String {
        val findMember = memberRepository.findByIdOrNull(userId)
            ?: throw InvalidInputException("존재하지 않는 회원입니다.")
        topicRepository.save(Topic(name = topicName, member = findMember))
        return "주제를 정상적으로 저장했습니다."
    }

    // Topic 수정
    @Transactional
    fun updateTopic(userId: Long, topicRequestDto: TopicRequestDto): String {
        val findTopic = topicRepository.findByIdOrNull(topicRequestDto.topicId)

        return if (findTopic != null) {
            // 수정할 주제가 존재할 경우
            findTopic.name = topicRequestDto.topicName
            topicRepository.save(findTopic) // 변경된 내용 저장
            return "수정이 완료되었습니다."
        } else {
            return "수정할 주제가 존재하지 않습니다."
        }
    }

    // Topic 삭제
    @Transactional
    fun deleteTopic(topidId: Long): String {
        topicRepository.deleteById(topidId)
        return "주제가 정상적으로 삭제되었습니다."
    }

    // 특정 멤버가 작성한 모든 Topic과 각 Topic에 속한 메모 개수 조회
    @Transactional(readOnly = true)
    fun getTopicsWithContentCountByMember(memberId: Long): List<TopicResponseDto> {
        return topicRepository.findByMemberId(memberId).map { topic ->
            TopicResponseDto(
                topicId = topic.id,
                topicName = topic.name,
                contentNum = topicContentRepository.findByMemberIdAndTopicId(memberId, topic.id).size
            )
        }
    }
}
