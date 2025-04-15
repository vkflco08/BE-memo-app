package com.memo.memo.common.exception.exceptions

import com.memo.memo.common.exception.CustomException
import org.springframework.http.HttpStatus

class FileUploadException(
    resultCode: String = "IMAGE_NOT_FOUND",
    message: String = "이미지를 찾을 수 없습니다.",
    httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED,
) : CustomException(resultCode, message, httpStatus)
