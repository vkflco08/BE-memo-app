package com.memo.memo.content.controller

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.CustomUser
import com.memo.memo.content.dto.ContentDtoRequest
import com.memo.memo.content.dto.ContentDtoResponse
import com.memo.memo.content.service.ContentService
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/memo")
class ContentController(
    private val contentService: ContentService,
) {
    /**
     * 메모 저장
     */
    @PostMapping("/new")
    fun signUp(
        @RequestBody @Valid contentDtoRequest: ContentDtoRequest,
    ): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")
        contentDtoRequest.memberId = userId
        val resultMsg: String = contentService.saveMemo(contentDtoRequest)
        return BaseResponse(message = resultMsg)
    }

    /**
     * 전체 메모 불러오기
     */
    @GetMapping("/all")
    fun getMemos(): BaseResponse<List<ContentDtoResponse>> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")
        val resultMsg: List<ContentDtoResponse> = contentService.getMemos(userId)
        return BaseResponse(data = resultMsg)
    }

    /**
     * 메모 불러오기
     * @param date 메모 날짜
     */
    @GetMapping("/{date}")
    fun getMemo(@PathVariable date: String): BaseResponse<ContentDtoResponse?> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")
        val resultMsg: ContentDtoResponse? = contentService.getMemo(userId, date)
        return BaseResponse(data = resultMsg)
    }

    /**
     * 메모 수정하기
     */
    @PutMapping("/edit")
    fun saveMyInfo(
        @RequestBody @Valid contentDtoRequest: ContentDtoRequest,
    ): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")
        contentDtoRequest.id = userId
        val resultMsg: String = contentService.editMemo(contentDtoRequest)
        return BaseResponse(message = resultMsg)
    }
}
