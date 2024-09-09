package com.memo.memo.common.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.memo.memo.common.entity.MemberRefreshToken

interface MemberRefreshTokenRepository : JpaRepository<MemberRefreshToken, Long>
