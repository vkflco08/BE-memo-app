package com.memo.memo.contentanalysis.controller

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.dto.CustomUser
import com.memo.memo.common.exception.exceptions.UserNotFoundException
import com.memo.memo.contentanalysis.dto.StatisticsResponseDto
import com.memo.memo.contentanalysis.service.StatisticsService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/statistics")
class StatisticsController(
    private val statisticsService: StatisticsService,
) {
    @GetMapping("/member")
    fun getStatistics(): BaseResponse<StatisticsResponseDto> {
        val userId =
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
                ?: throw UserNotFoundException()

        val probability = statisticsService.calculateMemoCreationProbability(userId)
        val avgTime = statisticsService.calculateAverageMemoCreationTime(userId)
        val memoLength = statisticsService.analyzeMemoLength(userId)

        return BaseResponse.success(StatisticsResponseDto(probability, avgTime, memoLength))
    }
}
