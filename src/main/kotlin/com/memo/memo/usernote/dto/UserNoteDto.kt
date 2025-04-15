package com.memo.memo.usernote.dto

import com.memo.memo.member.entity.Member
import com.memo.memo.usernote.entity.UserNote

data class UserNoteDto(
    var id: Long?,
    var title: String?,
    var content: String,
    val memberId: Long?,
) {
    fun toEntity(member: Member): UserNote =
        UserNote(
            id = null,
            // ID는 DB에서 자동생성
            title = title ?: "제목 없음",
            content = content,
            member = member,
        )
}
