package com.memo.memo.common.repository

import com.memo.memo.common.entity.MemberRefreshToken
import com.memo.memo.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRefreshTokenRepository : JpaRepository<MemberRefreshToken, Long> {
    fun findByMember(member: Member): MemberRefreshToken?
}
