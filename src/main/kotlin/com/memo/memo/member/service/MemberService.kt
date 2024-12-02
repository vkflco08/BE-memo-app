package com.memo.memo.member.service

import com.memo.memo.common.authority.JwtTokenProvider
import com.memo.memo.common.authority.TokenInfo
import com.memo.memo.common.exception.InvalidInputException
import com.memo.memo.common.repository.MemberRefreshTokenRepository
import com.memo.memo.common.service.SignService
import com.memo.memo.common.status.ROLE
import com.memo.memo.member.dto.LoginDto
import com.memo.memo.member.dto.MemberDtoRequest
import com.memo.memo.member.dto.MemberDtoResponse
import com.memo.memo.member.dto.MemberProfileDtoRequest
import com.memo.memo.member.entity.Member
import com.memo.memo.member.entity.MemberRole
import com.memo.memo.member.repository.MemberRepository
import com.memo.memo.member.repository.MemberRoleRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val signService: SignService,
    private val memberRefreshTokenRepository: MemberRefreshTokenRepository,
    private val fileService: FileService,
) {
    /**
     * 회원가입
     */
    @Transactional
    fun signUp(memberDtoRequest: MemberDtoRequest): String {
        var member = memberRepository.findByLoginId(memberDtoRequest.loginId)
        if (member != null) {
            throw InvalidInputException("loginId", "이미 등록된 ID 입니다.")
        }
        member = memberDtoRequest.toEntity()
        memberRepository.save(member)

        val memberRole = MemberRole(null, ROLE.MEMBER, member)
        memberRoleRepository.save(memberRole)

        return "회원가입이 완료되었습니다."
    }

    /**
     * 로그인 -> 토큰 발행
     */
    @Transactional
    fun login(loginDto: LoginDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.loginId, loginDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        // 권한 출력
        println("User Authorities: ${authentication.authorities}")

        val accessToken = jwtTokenProvider.createAccessToken(authentication)
        val refreshToken = jwtTokenProvider.createRefreshToken(authentication)

        val member = memberRepository.findByLoginId(loginDto.loginId)

        // Refresh Token 저장
        if (member != null) {
            signService.saveRefreshToken(member, refreshToken)
            return TokenInfo("Bearer", accessToken, refreshToken)
        }
        else{
            throw InvalidInputException("유저를 찾을 수 없습니다.")
        }

    }

    /**
     * 내정보 조회
     */
    fun searchMyInfo(userId: Long?): MemberDtoResponse {
        val member: Member = memberRepository.findByIdOrNull(userId)
            ?: throw InvalidInputException("회원 정보가 존재하지 않습니다.")

        val findImage = fileService.getProfileImage(member)

        return member.toDto(findImage)
    }

    /**
     * 내 정보 수정
     */
    @Transactional
    fun saveMyInfo(
        @RequestPart("profileImage") profileImage: MultipartFile?,
        memberProfileDtoRequest: MemberProfileDtoRequest
    ): MemberDtoResponse {
        val existingMember: Member = memberRepository.findByIdOrNull(memberProfileDtoRequest.id)
            ?: throw InvalidInputException("회원 정보가 존재하지 않습니다.")

        // 프로필 이미지 저장
        profileImage?.let {
            val savedPath = fileService.saveProfileImage(existingMember.id, it) // 파일 저장
            memberProfileDtoRequest.profileImage = savedPath // 저장 경로를 DTO에 설정
        }

        memberProfileDtoRequest.toEntity(existingMember)

        // 변경 감지로 업데이트 수행
        return existingMember.toDto()
    }

    /**
     * 로그아웃
     */
    @Transactional
    fun deleteRefToken(loginId: Long): String {
        memberRefreshTokenRepository.deleteById(loginId)
        return "로그아웃 되었습니다."
    }
}
