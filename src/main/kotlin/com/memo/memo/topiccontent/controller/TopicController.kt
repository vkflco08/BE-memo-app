package com.memo.memo.topiccontent.controller

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.CustomUser
import com.memo.memo.common.exception.exceptions.UserNotFoundException
import com.memo.memo.topiccontent.dto.TopicDto
import com.memo.memo.topiccontent.dto.TopicResponseDto
import com.memo.memo.topiccontent.service.TopicService
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
    private val topicService: TopicService,
) {
    @PostMapping("/new/{topicName}")
    fun createTopic(
        @PathVariable topicName: String,
    ): BaseResponse<Long> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()

        return BaseResponse.success(data = topicService.createTopic(userId, topicName))
    }

    @PutMapping("/edit")
    fun updateTopic(
        @RequestBody topicRequestDto: TopicDto,
    ): BaseResponse<Unit> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()

        return BaseResponse.success(message = topicService.updateTopic(userId, topicRequestDto))
    }

    @DeleteMapping("/{topicId}")
    fun deleteTopic(
        @PathVariable topicId: Long,
    ): BaseResponse<Unit> = BaseResponse.success(message = topicService.deleteTopic(topicId))

    // 주제번호 제목 갯수
    @GetMapping("/list")
    fun getTopicsWithContentCountByMember(): BaseResponse<List<TopicResponseDto>> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()

        return BaseResponse.success(data = topicService.getTopicsWithContentCountByMember(userId))
    }
}
