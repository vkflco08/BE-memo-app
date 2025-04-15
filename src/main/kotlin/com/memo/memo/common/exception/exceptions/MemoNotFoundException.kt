package com.memo.memo.common.exception.exceptions

import com.memo.memo.common.exception.CustomException
import org.springframework.http.HttpStatus

class MemoNotFoundException :
    CustomException(
        resultCode = "MEMO_NOT_FOUND",
        message = "메모를 찾을 수 없습니다.",
        httpStatus = HttpStatus.UNAUTHORIZED,
    )
