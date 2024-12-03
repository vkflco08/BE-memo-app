package com.memo.memo.member.service

import com.memo.memo.member.entity.Member
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
@Slf4j
class FileService {

    private val uploadDir = "uploads" // 기본 저장 경로

    init {
        try {
            Files.createDirectories(Paths.get("uploads"))
        } catch (e: FileSystemException) {
            // 로그 기록 및 적절한 예외 처리
            log.error("디렉토리를 생성할 수 없습니다: ${e.message}")
        }
    }

    fun saveProfileImage(userId: Long?, file: MultipartFile): String {
        // 유저별 디렉터리 생성 (절대 경로로)
        val userDir = Paths.get(uploadDir, "$userId").toAbsolutePath()  // uploadDir은 절대 경로로 설정되어 있다고 가정
        try {
            // 디렉토리 생성
            Files.createDirectories(userDir)
        } catch (e: FileSystemException) {
            log.error("디렉토리를 생성할 수 없습니다: ${e.message}")
            throw e
        }

        // 파일 이름 생성
        val fileName = file.originalFilename ?: "default.jpg"  // 파일 이름이 null일 경우 default 이름 설정
        val filePath = userDir.resolve(fileName)  // 경로 결합 시 resolve 사용

        // 로그 추가: 파일 경로 출력
        println("파일 경로: $filePath")

        // 파일 저장
        file.transferTo(filePath.toFile())

        return filePath.toString()  // 저장된 파일 경로 반환
    }


    fun encodeImageToBase64(imagePath: String): String {
        val path = Paths.get(imagePath)
        val imageBytes = Files.readAllBytes(path)
        return Base64.getEncoder().encodeToString(imageBytes)
    }

    fun getProfileImage(member: Member): String? {
        // 절대 경로로 수정 (여기서는 'uploads' 디렉터리가 프로젝트의 루트에 있다고 가정)
        val profileImagePath = Paths.get(uploadDir, "${member.id}").toAbsolutePath().toString()
        println("profileImagePath: $profileImagePath")  // 경로 확인

        // 폴더가 존재하는지 확인
        val memberFolder = File(profileImagePath)
        println("memberFolder: $memberFolder")
        println("memberFolder.exists(): ${memberFolder.exists()}")
        if (!memberFolder.exists() || !memberFolder.isDirectory) {
            // 폴더가 없으면 null 리턴
            println("프로필 이미지 폴더가 존재하지 않음: $profileImagePath")
            return null
        }

        // 이미지 파일 경로 생성 (이미지 이름이 member.profileImage에 있다고 가정)
        val profileImageFile = File( member.profileImage ?: "")

        // 이미지 파일이 존재하면 Base64로 인코딩
        println("profileImageFile: $profileImageFile")
        println("profileImageFile.exists(): ${profileImageFile.exists()}")

        return if (profileImageFile.exists()) {
            println("프로필 이미지 경로: ${profileImageFile.absolutePath}")  // 경로 확인
            encodeImageToBase64(profileImageFile.absolutePath)
        } else {
            // 이미지 파일이 없으면 null 리턴
            println("프로필 이미지 파일이 존재하지 않음: ${profileImageFile.absolutePath}")
            null
        }
    }
}
