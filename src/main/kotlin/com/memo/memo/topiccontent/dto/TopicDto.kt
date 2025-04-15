package com.memo.memo.topiccontent.dto

import com.memo.memo.member.entity.Member
import com.memo.memo.topiccontent.entity.Topic
import com.memo.memo.topiccontent.entity.TopicContent
import java.time.LocalDateTime

data class TopicDto(
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
) {
    fun toEntity(
        member: Member?,
        topic: Topic,
    ): TopicContent =
        TopicContent(
            title = title,
            content = content,
            member = member,
            topic = topic,
        )
}

data class TopicContentResponseDto(
    val contentId: Long? = null,
    val title: String? = null,
    val content: String? = null,
    val date: LocalDateTime? = null,
)
