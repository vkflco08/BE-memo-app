package com.memo.memo.topic_content.controller

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.CustomUser
import com.memo.memo.topic_content.dto.TopicRequestDto
import com.memo.memo.topic_content.dto.TopicResponseDto
import com.memo.memo.topic_content.service.TopicService
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
@RequestMapping("/api/topic")
class TopicController(
    private val topicService: TopicService
) {
    @PostMapping("/new/{topicName}")
    fun createTopic(@PathVariable topicName: String): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        return BaseResponse(message = topicService.createTopic(userId, topicName))
    }

    @PutMapping("/edit")
    fun updateTopic(@RequestBody topicRequestDto: TopicRequestDto): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        return BaseResponse(message = topicService.updateTopic(userId, topicRequestDto))
    }

    @DeleteMapping("/delete/{topicId}")
    fun deleteTopic(@PathVariable topicId: Long): BaseResponse<Unit> {
        return BaseResponse(message = topicService.deleteTopic(topicId))
    }

    // 주제번호 제목 갯수
    @GetMapping("/list")
    fun getTopicsWithContentCountByMember(): BaseResponse<List<TopicResponseDto>> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        return BaseResponse(data = topicService.getTopicsWithContentCountByMember(userId))
    }
}
