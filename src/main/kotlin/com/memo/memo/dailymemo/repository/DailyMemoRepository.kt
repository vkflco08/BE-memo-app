package com.memo.memo.dailymemo.repository

import com.memo.memo.dailymemo.entity.DailyMemo
import com.memo.memo.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface DailyMemoRepository : JpaRepository<DailyMemo, Long> {
    fun findAllByMember(
        findMember: Member,
        pageable: Pageable,
    ): Page<DailyMemo>

    fun findByMemberAndDate(
        member: Member,
        date: String,
    ): DailyMemo?

    @Query("SELECT c FROM DailyMemo c WHERE c.member = :member AND c.date LIKE CONCAT(:yearMonth, '%')")
    fun findAllByMemberAndYearMonth(
        @Param("member") member: Member,
        @Param("yearMonth") yearMonth: String,
    ): List<DailyMemo>

    fun deleteByMemberAndDate(
        member: Member,
        date: String,
    )

    @Query(
        "SELECT c FROM DailyMemo c WHERE c.member = :member AND " +
            "(c.title LIKE %:keyword% OR c.content LIKE %:keyword%)",
    )
    fun searchByMemberAndKeyword(
        @Param("member") member: Member,
        @Param("keyword") keyword: String,
        pageable: Pageable,
    ): Page<DailyMemo>

    fun countByMemberId(memberId: Long): Int

    fun findAllByMemberId(memberId: Long): List<DailyMemo>
}
