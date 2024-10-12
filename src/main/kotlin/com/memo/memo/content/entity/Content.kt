package com.memo.memo.content.entity

import com.memo.memo.content.dto.ContentDtoResponse
import com.memo.memo.content.dto.UserNoteDto
import com.memo.memo.member.entity.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import lombok.Getter

@Entity
@Getter
class Content(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(nullable = false, length = 30)
    var title: String,
    @Column(nullable = false, length = 1000)
    var content: String,
    @Column(nullable = false, length = 30)
    val date: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_content_member_id"))
    val member: Member,
){
    fun toDto(): ContentDtoResponse =
        ContentDtoResponse(title, content, date)
}

@Entity
@Getter
class UserNote(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(nullable = false, length = 1000)
    var content: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_user_note_member_id"))
    val member: Member // 유저와의 관계
) {
    fun toDto(): UserNoteDto =
        UserNoteDto(id, member.id, content)
}

