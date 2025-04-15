package com.memo.memo.daily_memo.controller

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.CustomUser
import com.memo.memo.common.exception.exceptions.UserNotFoundException
import com.memo.memo.daily_memo.dto.ContentDtoRequest
import com.memo.memo.daily_memo.dto.ContentDtoResponse
import com.memo.memo.daily_memo.service.DailyMemoService
import jakarta.validation.Valid
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
@RequestMapping("/api/memo")
class DailyMemoController(
    private val dailyMemoService: DailyMemoService,
) {
    /**
     * 메모 저장
     */
    @PostMapping("/new")
    fun saveMemo(
        @RequestBody @Valid contentDtoRequest: ContentDtoRequest,
    ): BaseResponse<Unit> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()
        contentDtoRequest.memberId = userId
        val resultMsg: String = dailyMemoService.saveMemo(contentDtoRequest)
        return BaseResponse.success(message = resultMsg)
    }

    /**
     * 전체 메모 불러오기 (페이징 적용)
     */
    //    @Cacheable(value = ["memos"], key = "#page + '-' + #size")
    @GetMapping("/all")
    fun getMemos(
        @RequestParam(defaultValue = "0") page: Int, // 기본값 0번째 페이지
        @RequestParam(defaultValue = "10") size: Int, // 기본값 10개의 항목
    ): BaseResponse<Page<ContentDtoResponse>> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()

        val resultMsg: Page<ContentDtoResponse> = dailyMemoService.getMemos(userId, page, size)
        return BaseResponse.success(data = resultMsg)
    }

    /**
     * 달에 해당하는 메모 리턴
     * @param yearMonth YYYY-MM
     */
    //    @Cacheable(value = ["memosByMonth"], key = "#yearMonth") // 캐시 이름은 "memosByMonth", key는 yearMonth로 설정
    @GetMapping("/memos/{yearMonth}")
    fun getMemosByMonth(
        @PathVariable yearMonth: String,
    ): BaseResponse<List<ContentDtoResponse>> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()

        val resultMsg: List<ContentDtoResponse> = dailyMemoService.getMemosByMonth(userId, yearMonth)
        return BaseResponse.success(data = resultMsg)
    }

    /**
     * 메모 불러오기
     * @param date 메모 날짜
     */
    @GetMapping("/{date}")
    fun getMemo(
        @PathVariable date: String,
    ): BaseResponse<ContentDtoResponse?> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()
        val resultMsg: ContentDtoResponse? = dailyMemoService.getMemo(userId, date)
        return BaseResponse.success(data = resultMsg)
    }

    /**
     * 메모 삭제하기
     * @param date 메모 날짜
     */
    @DeleteMapping("/{date}")
    fun removeMemo(
        @PathVariable date: String,
    ): BaseResponse<Unit?> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()
        val resultMsg: String = dailyMemoService.removeMemo(userId, date)
        return BaseResponse.success(message = resultMsg)
    }

    /**
     * 메모 수정하기
     */
    @PutMapping("/edit")
    fun saveMyInfo(
        @RequestBody @Valid contentDtoRequest: ContentDtoRequest,
    ): BaseResponse<Unit> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()
        contentDtoRequest.id = userId
        val resultMsg: String = dailyMemoService.editMemo(contentDtoRequest)
        return BaseResponse.success(message = resultMsg)
    }

    /**
     * 메모 검색하기
     */
    @GetMapping("/search")
    fun searchContents(
        @RequestParam keyword: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int, // 기본값 10개의 항목
    ): BaseResponse<Page<ContentDtoResponse>> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()

        return BaseResponse.success(data = dailyMemoService.searchContentByKeyword(userId, keyword, page, size))
    }
}
