package com.memo.memo.topic_content.controller

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.CustomUser
import com.memo.memo.content.dto.ContentDtoResponse
import com.memo.memo.topic_content.dto.TopicContentRequestDto
import com.memo.memo.topic_content.dto.TopicContentResponseDto
import com.memo.memo.topic_content.dto.TopicDto
import com.memo.memo.topic_content.service.TopicContentService
import org.springframework.data.domain.Page
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/topic-content")
class TopicContentController(
    private val topicContentService: TopicContentService
) {
    @PostMapping("/new")
    fun createTopicContent(
        @RequestBody topicContentRequestDto: TopicContentRequestDto
    ): BaseResponse<TopicDto> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        return BaseResponse(data = topicContentService.createTopicContent(userId, topicContentRequestDto))
    }

    @PutMapping("/edit")
    fun updateTopicContent(
        @RequestBody topicContentRequestDto: TopicContentRequestDto
    ): BaseResponse<TopicDto> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        return BaseResponse(data = topicContentService.updateTopicContent(userId, topicContentRequestDto))
    }

    // 특정 컨텐츠 불러오기
    @GetMapping("/{contentId}")
    fun getTopicContent(
        @PathVariable contentId: Long,
    ): BaseResponse<TopicContentResponseDto> {
        return BaseResponse(data = topicContentService.getTopicContent(contentId))
    }

    // 컨텐츠 번호
    @DeleteMapping("/{contentId}")
    fun deleteTopicContent(
        @PathVariable contentId: Long,
    ): BaseResponse<Unit> {
        return BaseResponse(message = topicContentService.deleteTopicContent(contentId))
    }

    // 주제에 해당하는 전체 콘텐츠 불러오기
    @GetMapping("/{topicId}/all")
    fun getAllTopicContentsByMemberAndTopic(
        @PathVariable topicId: Long,
        @RequestParam(defaultValue = "0") page: Int, // 기본값 0번째 페이지
        @RequestParam(defaultValue = "10") size: Int // 기본값 10개의 항목
    ): BaseResponse<Page<TopicContentResponseDto>> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        return BaseResponse(data = topicContentService.getAllTopicContentsByMemberAndTopic(userId, topicId, page, size))
    }

    /**
     * 주제별 메모 검색하기
     */
    @GetMapping("/{topicId}/search")
    fun searchTopicContents(
        @PathVariable topicId: Long,
        @RequestParam keyword: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int // 기본값 10개의 항목
    ): BaseResponse<Page<TopicContentResponseDto>> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        return BaseResponse(data = topicContentService.searchTopicContentByKeyword(userId, topicId, keyword, page, size))
    }

}