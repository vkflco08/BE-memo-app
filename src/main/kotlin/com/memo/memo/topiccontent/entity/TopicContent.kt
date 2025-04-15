package com.memo.memo.topiccontent.entity

import BaseEntity
import com.memo.memo.member.entity.Member
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
    @Column(nullable = false)
    var title: String? = null,
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String? = null,
    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member? = null,
    @ManyToOne
    @JoinColumn(name = "topic_id")
    var topic: Topic? = null,
) : BaseEntity()
