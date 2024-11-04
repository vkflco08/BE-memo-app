package com.memo.memo.content_analysis.controller

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.CustomUser
import com.memo.memo.content_analysis.dto.StatisticsResponseDto
import com.memo.memo.content_analysis.service.StatisticsService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/statistics")
class StatisticsController(private val statisticsService: StatisticsService) {
    @GetMapping("/member")
    fun getStatistics(): BaseResponse<StatisticsResponseDto> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
            ?: return BaseResponse(message = "유저를 찾을 수 없습니다")

        val probability = statisticsService.calculateMemoCreationProbability(userId)
        val avgTime = statisticsService.calculateAverageMemoCreationTime(userId)
        val memoLength = statisticsService.analyzeMemoLength(userId)
        return BaseResponse(data = StatisticsResponseDto(probability, avgTime, memoLength))
    }
}
