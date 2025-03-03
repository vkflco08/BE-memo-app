package com.memo.memo.usernote.entity

import BaseEntity
import com.memo.memo.member.entity.Member
import com.memo.memo.usernote.dto.UserNoteDto
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
class UserNote(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, length = 30)
    var title: String = "new user note",
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_user_note_member_id"))
    val member: Member, // 유저와의 관계
) : BaseEntity() {
    fun toDto(): UserNoteDto = UserNoteDto(id, title, content, member.id)

    fun update(
        title: String,
        content: String,
    ) {
        this.title = title
        this.content = content
    }
}
