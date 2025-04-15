package com.memo.memo.common.exception.exceptions

import com.memo.memo.common.exception.CustomException
import org.springframework.http.HttpStatus

class DirectoryCreateException :
    CustomException(
        resultCode = "DIRECTORY_CREATE_EXCEPTION",
        message = "디렉토리를 생성할 수 없습니다.",
        httpStatus = HttpStatus.UNAUTHORIZED,
    )
