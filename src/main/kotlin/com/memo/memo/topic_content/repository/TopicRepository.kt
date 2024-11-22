package com.memo.memo.topic_content.repository

import com.memo.memo.topic_content.entity.Topic
import org.springframework.data.jpa.repository.JpaRepository
import com.memo.memo.topic_content.entity.TopicContent

interface TopicRepository : JpaRepository<Topic, Long> {
    fun findByMemberId(memberId: Long): List<Topic>
    fun findTopicByName(topicName: String): Topic
}

interface TopicContentRepository : JpaRepository<TopicContent, Long> {
    fun findByMemberIdAndTopicId(memberId: Long, topicId: Long?): List<TopicContent>

}
