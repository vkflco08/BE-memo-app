package com.memo.memo.usernote.controller

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.CustomUser
import com.memo.memo.common.exception.exceptions.UserNotFoundException
import com.memo.memo.usernote.dto.UserNoteDto
import com.memo.memo.usernote.service.UserNoteService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user_note")
class UserNoteController(
    private val userNoteService: UserNoteService,
) {
    /**
     * 유저 노트 전체저장
     */
    @PostMapping("/user_notes")
    fun saveUsernotes(
        @RequestBody userNoteDto: List<UserNoteDto>,
    ): BaseResponse<Unit> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()
        val resultMsg: String = userNoteService.saveUserNotes(userNoteDto, userId)
        return BaseResponse.success(message = resultMsg)
    }

    /**
     * 유저 노트 저장
     */
    @PostMapping("/new")
    fun saveUsernote(
        @RequestBody userNoteDto: UserNoteDto,
    ): BaseResponse<Unit> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()
        val resultMsg: String = userNoteService.saveUserNote(userNoteDto, userId)
        return BaseResponse.success(message = resultMsg)
    }

    /**
     * 유저 노트 삭제
     */
    @DeleteMapping("/{noteId}")
    fun deleteUsernote(
        @PathVariable noteId: Long,
    ): BaseResponse<List<UserNoteDto>?> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()
        userNoteService.deleteUserNote(noteId, userId)
        val resultMsg: List<UserNoteDto>? = userNoteService.getUserNote(userId)
        return BaseResponse.success(data = resultMsg)
    }

    //    @Cacheable(value = ["userNote"], key = "#userId") // 캐시 이름은 "userNote", key는 userId로 설정
    @GetMapping("/")
    fun getUsernote(): BaseResponse<List<UserNoteDto>?> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()

        val resultMsg: List<UserNoteDto>? = userNoteService.getUserNote(userId)
        return BaseResponse.success(data = resultMsg)
    }
}
