package com.memo.memo.content_analysis.service

import com.memo.memo.content.repository.ContentRepository
import com.memo.memo.content_analysis.dto.MemoAverageCreationStats
import com.memo.memo.content_analysis.dto.MemoLengthStats
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
        val member = memberRepository.findById(memberId).orElseThrow { IllegalArgumentException("존재하지 않는 사용자입니다.") }
        val totalDays = ChronoUnit.DAYS.between(member.createdDate, LocalDateTime.now()).toDouble()

        if (totalDays == 0.0) return 0.0 // 회원가입 당일일 경우
        val memoCount = contentRepository.countByMemberId(memberId).toDouble()
        return (memoCount / totalDays) * 100 // 확률을 백분율로 반환
    }

    // 2. 평균 메모 작성 시간 계산
    fun calculateAverageMemoCreationTime(memberId: Long): MemoAverageCreationStats {
        val memos = contentRepository.findAllByMemberId(memberId)
        if (memos.isEmpty()) return MemoAverageCreationStats(
            0.0, 0.0, 0.0, emptyList()
        )

        // 메모 생성 시간을 리스트로 추출
        val creationTimes = memos.map { memo -> memo.createdDate }

        // 시간, 분, 초의 총합 계산
        val totalHours = creationTimes.sumOf { it!!.hour }
        val totalMinutes = creationTimes.sumOf { it!!.minute }
        val totalSeconds = creationTimes.sumOf { it!!.second }

        // 각 평균 계산
        val averageHours = totalHours.toDouble() / memos.size
        val averageMinutes = totalMinutes.toDouble() / memos.size
        val averageSeconds = totalSeconds.toDouble() / memos.size

        // 생성일 리스트 생성
        val creationDates = memos.map { memo -> memo.createdDate ?: LocalDateTime.now() } // null 체크

        return MemoAverageCreationStats(
            averageHours = averageHours,
            averageMinutes = averageMinutes,
            averageSeconds = averageSeconds,
            creationTimes = creationDates
        )
    }

    // 3. 메모 길이 분석
    fun analyzeMemoLength(memberId: Long): MemoLengthStats {
        val memos = contentRepository.findAllByMemberId(memberId)
        if (memos.isEmpty()) return MemoLengthStats(averageLength = 0.0, lengths = emptyList())

        val memoLengths = memos.map { it.content.length }
        val averageLength = memoLengths.average()

        return MemoLengthStats(
            averageLength = averageLength,
            lengths = memoLengths // 각 메모 길이 리스트를 포함
        )
    }
}
