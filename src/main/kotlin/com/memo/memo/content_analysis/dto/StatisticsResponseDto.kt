package com.memo.memo.content_analysis.dto

import java.time.LocalDateTime

class StatisticsResponseDto (
    val memoCreationProbability: Double, // 작성 확률 (%)
    val averageMemoCreationTime: MemoAverageCreationStats,
    val memoLengthStats: MemoLengthStats,
)

// 메모 길이 분석
data class MemoLengthStats(
    val averageLength: Double,
    val lengths: List<Int> // 각 메모의 길이 리스트
)

// 메모 평군 생성 시간 분석
data class MemoAverageCreationStats(
    val averageHours: Double,
    val averageMinutes: Double,
    val averageSeconds: Double,
    val creationTimes: List<LocalDateTime>,
)
