package com.memo.memo.content.dto

import com.memo.memo.content.entity.Content
import com.memo.memo.member.entity.Member

data class ContentDtoRequest (
    var id: Long,
    val title: String,
    val content: String,
    val date: String, // yyyy-MM-dd 형식의 문자열
    ){
    fun toEntity(member: Member): Content = Content(id, title, content, date, member)
}