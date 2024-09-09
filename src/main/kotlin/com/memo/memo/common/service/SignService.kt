package com.memo.memo.common.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import com.memo.memo.common.authority.JwtTokenProvider
import com.memo.memo.common.authority.TokenInfo
import com.memo.memo.common.dto.TokenDtoRequest
import com.memo.memo.common.entity.MemberRefreshToken
import com.memo.memo.common.exception.InvalidInputException
import com.memo.memo.common.repository.MemberRefreshTokenRepository
import com.memo.memo.member.entity.Member

@Service
class SignService(
    private val memberRefreshTokenRepository: MemberRefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    @Transactional
    fun saveRefreshToken(
        member: Member,
        refreshToken: String,
    ) {
        memberRefreshTokenRepository.save(MemberRefreshToken(member, refreshToken))
    }

    fun newAccessToken(tokenDtoRequest: TokenDtoRequest): TokenInfo {
        val findRefreshToken = memberRefreshTokenRepository.findById(tokenDtoRequest.memberId).get().refreshToken

        // Refresh token 검증
        if (!jwtTokenProvider.validateToken(findRefreshToken)) {
            throw InvalidInputException("로그인이 만료되었습니다.")
        }

        if (tokenDtoRequest.refreshToken == findRefreshToken) {
            // Refresh token으로부터 사용자 정보 추출
            val authentication = jwtTokenProvider.getAuthentication(findRefreshToken)
            // 새로운 Access token 생성
            val newAccessToken = jwtTokenProvider.createAccessToken(authentication)

            return TokenInfo("Bearer", newAccessToken, findRefreshToken)
        } else {
            throw InvalidInputException("다시 로그인해주세요")
        }
    }
}
