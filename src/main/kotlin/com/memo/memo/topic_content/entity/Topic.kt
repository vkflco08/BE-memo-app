package com.memo.memo.topic_content.entity

import BaseEntity
import com.memo.memo.member.entity.Member
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import lombok.Setter

@Entity
@Setter
class Topic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 30)
    var name: String? = null, // 주제 이름
    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member? = null, // 주제를 생성한 사용자
    @OneToMany(mappedBy = "topic", cascade = [CascadeType.ALL])
    var memos: Set<TopicContent>? = null, // 주제에 속한 메모들
) : BaseEntity()
