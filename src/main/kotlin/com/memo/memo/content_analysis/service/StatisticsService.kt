package com.memo.memo.content_analysis.service

import com.memo.memo.common.exception.exceptions.UserNotFoundException
import com.memo.memo.content_analysis.dto.MemoAverageCreationStats
import com.memo.memo.content_analysis.dto.MemoLengthStats
import com.memo.memo.daily_memo.repository.DailyMemoRepository
import com.memo.memo.member.repository.MemberRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class StatisticsService(
    private val dailyMemoRepository: DailyMemoRepository,
    private val memberRepository: MemberRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(StatisticsService::class.java)

    // 1. 메모 작성 확률 계산
    fun calculateMemoCreationProbability(memberId: Long): List<Double> {
        logger.info("Calculating memo creation probability for member ID: {}", memberId)
        val member =
            memberRepository
                .findById(memberId)
                .orElseThrow { UserNotFoundException() }

        val totalDays = ChronoUnit.DAYS.between(member.createdDate, LocalDateTime.now()).toDouble()

        if (totalDays == 0.0) {
            return listOf(0.0) // 회원가입 당일일 경우
        }

        val memoCount = dailyMemoRepository.countByMemberId(memberId).toDouble()
        val probability =
            if (totalDays != 0.0) {
                (memoCount / totalDays) * 100 // 확률을 백분율로 반환
            } else {
                0.0
            }
        logger.info("Calculated probability: {}%", probability)
        return listOf(probability, memoCount)
    }

    // 2. 평균 메모 작성 시간 계산
    fun calculateAverageMemoCreationTime(memberId: Long): MemoAverageCreationStats {
        logger.info("Calculating average memo creation time for member ID: {}", memberId)
        val memos = dailyMemoRepository.findAllByMemberId(memberId)

        if (memos.isEmpty()) {
            throw UserNotFoundException()
        }

        try {
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

            logger.info(
                "Calculated average creation time - Hours: {}, Minutes: {}, Seconds: {}",
                averageHours,
                averageMinutes,
                averageSeconds,
            )
            return MemoAverageCreationStats(
                averageHours = averageHours,
                averageMinutes = averageMinutes,
                averageSeconds = averageSeconds,
                creationTimes = creationDates,
            )
        } catch (e: Exception) {
            logger.error("Error calculating average memo creation time for member ID: {}", memberId, e)
            throw RuntimeException("메모 생성 시간 계산 중 오류가 발생했습니다.")
        }
    }

    // 3. 메모 길이 분석
    fun analyzeMemoLength(memberId: Long): MemoLengthStats {
        logger.info("Analyzing memo length for member ID: {}", memberId)
        val memos = dailyMemoRepository.findAllByMemberId(memberId)

        if (memos.isEmpty()) {
            throw UserNotFoundException() // 예외 처리 추가
        }

        val memoLengths = memos.map { it.content.length }
        val averageLength = memoLengths.average()

        logger.info("Calculated average memo length: {}", averageLength)
        return MemoLengthStats(
            averageLength = averageLength,
            lengths = memoLengths, // 각 메모 길이 리스트를 포함
        )
    }
}
