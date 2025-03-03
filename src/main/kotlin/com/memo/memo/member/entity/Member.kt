package com.memo.memo.member.entity

import BaseEntity
import com.memo.memo.common.status.ROLE
import com.memo.memo.member.dto.MemberDtoResponse
import com.memo.memo.usernote.entity.UserNote
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import lombok.Getter

@Entity
@Getter
@Table(
    uniqueConstraints = [UniqueConstraint(name = "uk_member_login_id", columnNames = ["loginId"])],
)
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(nullable = false, length = 30, updatable = false)
    val loginId: String,
    @Column(nullable = false, length = 100)
    val password: String,
    @Column(nullable = false, length = 10)
    var name: String,
    @Column(nullable = false, length = 30)
    var email: String,
    @Column(nullable = true)
    var profileImage: String? = null,
) : BaseEntity() {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userNotes: MutableList<UserNote> = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    val memberRole: List<MemberRole>? = null

    fun toDto(): MemberDtoResponse =
        MemberDtoResponse(
            id = this.id!!,
            loginId = this.loginId,
            name = this.name,
            email = this.email,
            profileImage = this.profileImage,
            createdDate = this.createdDate,
        )

    fun toDto(profileImageBase64: String?): MemberDtoResponse =
        MemberDtoResponse(id!!, loginId, name, email, profileImage, profileImageBase64, createdDate)
}

@Entity
class MemberRole(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val role: ROLE,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_member_role_member_id"))
    val member: Member,
) : BaseEntity()
