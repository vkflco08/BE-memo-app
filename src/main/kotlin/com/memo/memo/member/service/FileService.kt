package com.memo.memo.member.service

import com.memo.memo.member.dto.MemberDtoResponse
import com.memo.memo.member.entity.Member
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class FileService {

    private val uploadDir = "/uploads" // 기본 저장 경로

    init {
        Files.createDirectories(Paths.get(uploadDir)) // 최상위 디렉터리 생성
    }

    fun saveProfileImage(userId: Long?, file: MultipartFile): String {
        // 유저별 디렉터리 생성
        val userDir = "$uploadDir/$userId"
        Files.createDirectories(Paths.get(userDir))

        // 파일 이름 생성
        val extension = file.originalFilename?.substringAfterLast(".") ?: "jpg"
        val fileName = "${UUID.randomUUID()}.$extension"
        val filePath = "$userDir/$fileName"

        // 파일 저장
        file.transferTo(File(filePath))

        return filePath // 저장된 파일 경로 반환
    }

    fun encodeImageToBase64(imagePath: String): String {
        val path = Paths.get(imagePath)
        val imageBytes = Files.readAllBytes(path)
        return Base64.getEncoder().encodeToString(imageBytes)
    }

    fun getProfileImage(member: Member): String? {
        val profileImagePath = "${uploadDir}/${member.id}"  // 예: /opt/profile-images/1
        val profileImageBase64 = member.profileImage?.let {
            encodeImageToBase64(profileImagePath + "/$it")
        }
        return profileImageBase64
    }
}
