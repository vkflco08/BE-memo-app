package com.memo.memo.common.exception

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.exception.exceptions.InvalidInputException
import com.memo.memo.common.status.ResultCode
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }

        val response =
            BaseResponse.error(
                code = ResultCode.INVALID_INPUT.code,
                message = ResultCode.INVALID_INPUT.msg,
                status = ResultCode.INVALID_INPUT.status,
                data = errors,
            )

        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidInputException::class)
    fun handleInvalidInputException(ex: InvalidInputException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Invalid input"))

        val response =
            BaseResponse.error(
                code = ResultCode.INVALID_INPUT.code,
                message = ResultCode.INVALID_INPUT.msg,
                status = ResultCode.INVALID_INPUT.status,
                data = errors,
            )

        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): ResponseEntity<BaseResponse<Unit>> {
        val response =
            BaseResponse.error<Unit>(
                code = ResultCode.UNAUTHORIZED.code,
                message = "아이디 혹은 비밀번호를 다시 확인하세요.",
                status = ResultCode.UNAUTHORIZED.status,
            )
        return ResponseEntity(response, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(ex: ExpiredJwtException): ResponseEntity<BaseResponse<Unit>> {
        val response =
            BaseResponse.error<Unit>(
                code = ResultCode.TOKEN_EXPIRED.code,
                message = ResultCode.TOKEN_EXPIRED.msg,
                status = ResultCode.TOKEN_EXPIRED.status,
            )
        return ResponseEntity(response, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnhandledException(ex: Exception): ResponseEntity<BaseResponse<Unit>> {
        ex.printStackTrace() // 서버 로그에 출력

        val response =
            BaseResponse.error<Unit>(
                code = ResultCode.SERVER_ERROR.code,
                message = "예기치 못한 오류가 발생했습니다.",
                status = ResultCode.SERVER_ERROR.status,
            )

        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
