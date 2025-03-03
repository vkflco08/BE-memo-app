package com.memo.memo.daily_memo.dto

import com.memo.memo.daily_memo.entity.DailyMemo
import com.memo.memo.member.entity.Member

data class ContentDtoRequest(
    var id: Long,
    var memberId: Long,
    val title: String,
    val content: String,
    val date: String, // yyyy-MM-dd 형식의 문자열
) {
    fun toEntity(member: Member): DailyMemo = DailyMemo(id, title, content, date, member)
}

data class ContentDtoResponse(
    var title: String,
    var content: String,
    val date: String,
)
