package com.memo.memo.common.exception.exceptions

import com.memo.memo.common.exception.CustomException
import org.springframework.http.HttpStatus

class TopicNotFoundException :
    CustomException(
        resultCode = "TOPIC_NOT_FOUND",
        message = "주제를 찾을 수 없습니다.",
        httpStatus = HttpStatus.UNAUTHORIZED,
    )
