package com.memo.memo.common.controller

import com.memo.memo.common.authority.TokenInfo
import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.TokenDtoRequest
import com.memo.memo.common.service.SignService
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val signService: SignService,
) {
    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody tokenRequestDto: TokenDtoRequest,
    ): BaseResponse<TokenInfo> {
        val newAccessToken = signService.newAccessToken(tokenRequestDto)
        return BaseResponse(data = newAccessToken)
    }

    @GetMapping("/roles")
    fun getUserRoles(authentication: Authentication): Set<String> {
        return authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.toSet())
    }
}
