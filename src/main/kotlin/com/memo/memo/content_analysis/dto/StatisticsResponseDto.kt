package com.memo.memo.content_analysis.dto

import java.time.Duration

class StatisticsResponseDto (
    val memoCreationProbability: Double, // 작성 확률 (%)
    val averageMemoCreationTime: Duration // 평균 작성 시간
)