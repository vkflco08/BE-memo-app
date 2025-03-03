package com.memo.memo.usernote.repository

import com.memo.memo.member.entity.Member
import com.memo.memo.usernote.entity.UserNote
import org.springframework.data.jpa.repository.JpaRepository

interface UserNoteRepository : JpaRepository<UserNote, Long> {
    fun findAllByMember(findMember: Member): List<UserNote>?
}
