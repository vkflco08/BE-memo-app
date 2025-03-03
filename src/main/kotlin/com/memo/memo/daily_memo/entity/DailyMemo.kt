package com.memo.memo.daily_memo.entity

import BaseEntity
import com.memo.memo.daily_memo.dto.ContentDtoResponse
import com.memo.memo.member.entity.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import lombok.Getter

@Entity
@Getter
class DailyMemo(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(nullable = false, length = 30)
    var title: String,
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,
    @Column(nullable = false, length = 30)
    val date: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_daily_memo_member_id"))
    val member: Member,
) : BaseEntity() {
    fun toDto(): ContentDtoResponse = ContentDtoResponse(title, content, date)
}
