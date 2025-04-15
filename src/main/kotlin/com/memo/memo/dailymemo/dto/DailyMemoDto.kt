package com.memo.memo.dailymemo.dto

import com.memo.memo.dailymemo.entity.DailyMemo
import com.memo.memo.member.entity.Member

data class ContentDtoRequest(
    var id: Long,
    var memberId: Long,
    val title: String,
    val content: String,
    val date: String,
) {
    fun toEntity(member: Member): DailyMemo = DailyMemo(id, title, content, date, member)
}

data class ContentDtoResponse(
    var title: String,
    var content: String,
    val date: String,
)
