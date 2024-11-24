package com.memo.memo.topic_content.repository

import com.memo.memo.content.entity.Content
import com.memo.memo.member.entity.Member
import com.memo.memo.topic_content.entity.Topic
import org.springframework.data.jpa.repository.JpaRepository
import com.memo.memo.topic_content.entity.TopicContent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TopicRepository : JpaRepository<Topic, Long> {
    fun findByMemberId(memberId: Long): List<Topic>
    fun findTopicByName(topicName: String): Topic
}

interface TopicContentRepository : JpaRepository<TopicContent, Long> {
    fun countByMemberAndTopic(memberId: Member, topic: Topic): Int

    fun findByMemberIdAndTopicId(memberId: Long, topicId: Long?, pageable: Pageable): Page<TopicContent>

    @Query("Select p from TopicContent p where p.topic.id = :topicId AND " +
            "p.member.id = :userId AND " +
            "p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    fun searchTopicContentByMemberAndKeyword(
        @Param("userId") userId: Long,
        @Param("topicId") topicId: Long?,
        @Param("keyword") keyword: String,
        pageable: Pageable,
        ): Page<TopicContent>
}
