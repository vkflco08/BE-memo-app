package com.memo.memo.content.repository

import com.memo.memo.content.entity.Content
import com.memo.memo.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface ContentRepository : JpaRepository<Content, Long> {
    fun findAllByMember(findMember: Member): List<Content>
    fun findByMemberAndDate(member: Member, date: String): Content?
}
