package com.memo.memo.member.controller

import com.memo.memo.common.authority.TokenInfo
import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.CustomUser
import com.memo.memo.member.dto.LoginDto
import com.memo.memo.member.dto.MemberDtoRequest
import com.memo.memo.member.dto.MemberDtoResponse
import com.memo.memo.member.dto.MemberProfileDtoRequest
import com.memo.memo.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberService: MemberService,
) {
    /**
     * 회원가입
     */
    @PostMapping("/signup")
    fun signUp(
        @RequestBody @Valid memberDtoRequest: MemberDtoRequest,
    ): BaseResponse<Unit> {
        val resultMsg: String = memberService.signUp(memberDtoRequest)
        return BaseResponse(message = resultMsg)
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    fun login(
        @RequestBody @Valid loginDto: LoginDto,
    ): BaseResponse<TokenInfo> {
        val tokenInfo = memberService.login(loginDto)
        return BaseResponse(data = tokenInfo)
    }

    /**
     * 내 정보 보기
     */
    @GetMapping("/info")
    fun searchMyInfo(): BaseResponse<MemberDtoResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val response = memberService.searchMyInfo(userId)
        return BaseResponse(data = response)
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/info_edit")
    fun saveMyInfo(
        @RequestPart("profileImage") profileImage: MultipartFile?,
        @RequestBody @Valid memberProfileDtoRequest: MemberProfileDtoRequest
    ): BaseResponse<MemberDtoResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        memberProfileDtoRequest.id = userId
        val resultMsg = memberService.saveMyInfo(profileImage, memberProfileDtoRequest)
        return BaseResponse(data = resultMsg)
    }


    /**
     * 로그아웃
     */
    @DeleteMapping("/logout")
    fun logout(): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        val resultMsg: String = memberService.deleteRefToken(userId)
        return BaseResponse(message = resultMsg)
    }
}
