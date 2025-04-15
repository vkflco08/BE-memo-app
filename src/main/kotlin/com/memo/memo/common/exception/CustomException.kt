package com.memo.memo.common.exception

import org.springframework.http.HttpStatus

open class CustomException(
    val resultCode: String,
    override val message: String,
    val httpStatus: HttpStatus,
) : RuntimeException(message)
