package com.memo.memo.content.repository

import com.memo.memo.content.entity.Content
import com.memo.memo.content.entity.UserNote
import com.memo.memo.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ContentRepository : JpaRepository<Content, Long> {
    fun findAllByMember(findMember: Member, pageable: Pageable): Page<Content>
    fun findByMemberAndDate(member: Member, date: String): Content?

    @Query("SELECT c FROM Content c WHERE c.member = :member AND c.date LIKE CONCAT(:yearMonth, '%')")
    fun findAllByMemberAndYearMonth(@Param("member") member: Member, @Param("yearMonth") yearMonth: String): List<Content>
    fun deleteByMemberAndDate(member: Member, date: String)

    @Query("SELECT c FROM Content c WHERE c.member = :member AND " +
            "(c.title LIKE %:keyword% OR c.content LIKE %:keyword%)")
    fun searchByMemberAndKeyword(
        @Param("member") member: Member,
        @Param("keyword") keyword: String,
        pageable: Pageable
    ): Page<Content>

    fun countByMemberId(memberId: Long): Int
    fun findAllByMemberId(memberId: Long): List<Content>
}

interface UserNoteRepository : JpaRepository<UserNote, Long> {
    fun findByMember(findMember: Member): UserNote?
}