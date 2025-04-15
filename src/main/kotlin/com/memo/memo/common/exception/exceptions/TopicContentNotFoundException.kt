package com.memo.memo.common.exception.exceptions

import com.memo.memo.common.exception.CustomException
import org.springframework.http.HttpStatus

class TopicContentNotFoundException :
    CustomException(
        resultCode = "TOPIC_CONTENT_NOT_FOUND",
        message = "주제에 대한 글을 찾을 수 없습니다.",
        httpStatus = HttpStatus.UNAUTHORIZED,
    )
