package com.memo.memo.topic_content.entity
import BaseEntity
import com.memo.memo.member.entity.Member
import com.memo.memo.topic_content.dto.TopicContentResponseDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne

@Entity
class TopicContent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 30)
    var title: String? = null,

    @Lob
    @Column(nullable = false)
    var content: String? = null,

    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member? = null, // 메모를 작성한 사용자

    @ManyToOne
    @JoinColumn(name = "topic_id")
    var topic: Topic? = null, // 메모가 속한 주제 (하나만 가능)
): BaseEntity()
