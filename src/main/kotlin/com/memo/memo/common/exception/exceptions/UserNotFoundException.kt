package com.memo.memo.common.exception.exceptions

import com.memo.memo.common.exception.CustomException
import org.springframework.http.HttpStatus

class UserNotFoundException :
    CustomException(
        resultCode = "USER_NOT_FOUND",
        message = "유저를 찾을 수 없습니다.",
        httpStatus = HttpStatus.UNAUTHORIZED,
    )
