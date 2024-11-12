package com.memo.memo.topic_content.controller

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.CustomUser
import com.memo.memo.topic_content.dto.TopicContentRequestDto
import com.memo.memo.topic_content.dto.TopicContentResponseDto
import com.memo.memo.topic_content.service.TopicContentService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/topic-content")
class TopicContentController(
    private val topicContentService: TopicContentService
) {
    @PostMapping("/new")
    fun createTopicContent(
        @RequestBody topicContentRequestDto: TopicContentRequestDto
    ): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        return BaseResponse(message = topicContentService.createTopicContent(userId, topicContentRequestDto))
    }

    @PutMapping("/edit")
    fun updateTopicContent(
        @RequestBody topicContentRequestDto: TopicContentRequestDto
    ): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        return BaseResponse(message = topicContentService.updateTopicContent(userId, topicContentRequestDto))
    }

    // 컨텐츠 번호
    @DeleteMapping("/delete/{id}")
    fun deleteTopicContent(
        @PathVariable contentId: Long,
    ): BaseResponse<Unit> {
        return BaseResponse(message = topicContentService.deleteTopicContent(contentId))
    }

    // 주제 번호
    @GetMapping("/{topicId}")
    fun getAllTopicContentsByMemberAndTopic(
        @PathVariable topicId: Long,
    ): BaseResponse<List<TopicContentResponseDto>> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        return BaseResponse(data = topicContentService.getAllTopicContentsByMemberAndTopic(userId, topicId))
    }
}