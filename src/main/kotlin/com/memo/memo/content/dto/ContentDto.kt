package com.memo.memo.content.dto

import com.memo.memo.content.entity.Content
import com.memo.memo.content.entity.UserNote
import com.memo.memo.member.entity.Member

data class ContentDtoRequest(
    var id: Long,
    var memberId: Long,
    val title: String,
    val content: String,
    val date: String, // yyyy-MM-dd 형식의 문자열
) {
    fun toEntity(member: Member): Content = Content(id, title, content, date, member)
}

data class ContentDtoResponse(
    var title: String,
    var content: String,
    val date: String,
)

data class UserNoteDto(
    var id: Long?,
    var title: String?,
    var content: String,
    val memberId: Long?,
) {
    fun toEntity(member: Member): UserNote =
        UserNote(
            id = null, // ID는 DB에서 자동생성
            title = title ?: "제목 없음", // 기본값 설정
            content = content,
            member = member,
        )
}
