package com.memo.memo.member.controller

import com.memo.memo.common.dto.BaseResponse
import com.memo.memo.common.exception.exceptions.FileUploadException
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@RestController
class FileController {
    private val uploadDir = "uploads" // 이미지 파일이 저장된 디렉터리

    // 이미지 제공 엔드포인트 (유저별로 하나의 이미지 제공)
    @GetMapping("/uploads/{userId}")
    fun getImage(
        @PathVariable userId: Long,
    ): ResponseEntity<FileSystemResource> {
        val userDir = Paths.get(uploadDir, "$userId").toAbsolutePath()
        val userFolder = File(userDir.toString())

        if (userFolder.exists() && userFolder.isDirectory) {
            val imageFile = userFolder.listFiles()?.firstOrNull { it.isFile }
            if (imageFile != null && imageFile.exists()) {
                val filePath = File("$uploadDir/$userId/${imageFile.name}")
                val resource = FileSystemResource(filePath)

                // 파일의 MIME 타입을 probeContentType으로 자동 감지
                val contentType = Files.probeContentType(filePath.toPath()) ?: "application/octet-stream"

                // 이미지 파일을 반환하면서 Content-Type 설정
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource)
            }
        }

        // 이미지가 없으면 404 반환
        throw FileUploadException()
    }

    @GetMapping("/uploads/{userId}/{filename}")
    fun getProfileImage(
        @PathVariable userId: Long,
        @PathVariable filename: String,
    ): BaseResponse<File> {
        // 사용자 폴더 경로 생성
        val filePath = File("$uploadDir/$userId/$filename")
        return BaseResponse.success(data = filePath)
    }
}
