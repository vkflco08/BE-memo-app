package com.memo.memo.topic_content.dto

import com.memo.memo.member.entity.Member
import com.memo.memo.topic_content.entity.Topic
import com.memo.memo.topic_content.entity.TopicContent

data class TopicRequestDto(
    val topicId: Long,
    val topicName: String,
)

data class TopicResponseDto(
    val topicId: Long? = null,
    val topicName: String? = null,
    val contentNum: Int? = null,
)

data class TopicContentRequestDto(
    val topicId: Long,
    val contentId: Long?,
    val title: String,
    val content: String,
){
    fun toEntity(
        member: Member?,
        topic: Topic,
    ): TopicContent
            = TopicContent(
        title = title,
        content = content,
        member = member,
        topic = topic,
    )
}

data class TopicContentResponseDto(
    val title: String? = null,
    val content: String? = null,
)

