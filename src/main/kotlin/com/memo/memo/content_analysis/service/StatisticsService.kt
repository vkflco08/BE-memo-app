package com.memo.memo.content_analysis.service

import com.memo.memo.content.repository.ContentRepository
import com.memo.memo.member.repository.MemberRepository
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class StatisticsService(
    private val contentRepository: ContentRepository,
    private val memberRepository: MemberRepository
) {
    // 1. 메모 작성 확률 계산
    fun calculateMemoCreationProbability(memberId: Long): Double {
        print("calculateMemoCreationProbability 실행됨")  // 디버그용 print
        val member = memberRepository.findById(memberId).orElseThrow { IllegalArgumentException("존재하지 않는 사용자입니다.") }
        val totalDays = ChronoUnit.DAYS.between(member.createdDate, LocalDateTime.now()).toDouble()

        if (totalDays == 0.0) return 0.0 // 회원가입 당일일 경우
        val memoCount = contentRepository.countByMemberId(memberId).toDouble()
        return (memoCount / totalDays) * 100 // 확률을 백분율로 반환
    }

    // 2. 평균 메모 작성 시간 계산
    fun calculateAverageMemoCreationTime(memberId: Long): Duration {
        print("calculateAverageMemoCreationTime 실행됨")  // 디버그용 print
        val memos = contentRepository.findAllByMemberId(memberId)
        if (memos.isEmpty()) return Duration.ZERO

        val totalDuration = memos.fold(Duration.ZERO) { acc, memo ->
            acc.plus(Duration.between(memo.member.createdDate, memo.createdDate))
        }
        return totalDuration.dividedBy(memos.size.toLong())
    }
}
