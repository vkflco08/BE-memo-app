package com.memo.memo.common.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.memo.memo.common.entity.MemberRefreshToken
import com.memo.memo.member.entity.Member

interface MemberRefreshTokenRepository : JpaRepository<MemberRefreshToken, Long> {
    fun findByMember(member: Member): MemberRefreshToken?
}
