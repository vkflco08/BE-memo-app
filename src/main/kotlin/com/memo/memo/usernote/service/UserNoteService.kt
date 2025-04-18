package com.memo.memo.usernote.service

import com.memo.memo.common.exception.exceptions.InvalidInputException
import com.memo.memo.member.entity.Member
import com.memo.memo.member.repository.MemberRepository
import com.memo.memo.usernote.dto.UserNoteDto
import com.memo.memo.usernote.entity.UserNote
import com.memo.memo.usernote.repository.UserNoteRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserNoteService(
    private val userNoteRepository: UserNoteRepository,
    private val memberRepository: MemberRepository,
) {
    /**
     * 유저노트 전체 저장
     */
    @Transactional
    fun saveUserNotes(
        userNoteDto: List<UserNoteDto>,
        userId: Long,
    ): String {
        val findMember: Member =
            memberRepository.findByIdOrNull(userId)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")

        val existingNotes = userNoteRepository.findAllByMember(findMember) // 기존 노트 조회
        val existingNoteMap = existingNotes?.associateBy { it.id } ?: emptyMap() // ID 기준으로 매핑

        val newNotes = mutableListOf<UserNote>() // 새로 추가될 노트들을 담을 리스트

        userNoteDto.forEach { dto ->
            if (dto.id != null && existingNoteMap.containsKey(dto.id)) {
                // 기존 노트가 존재하면 업데이트
                val existingNote = existingNoteMap[dto.id]

                existingNote?.update(dto.title ?: "new user note", dto.content)
                existingNote?.let { newNotes.add(it) } // 업데이트된 노트를 newNotes에 추가
            } else {
                newNotes.add(dto.toEntity(findMember))
            }
        }

        userNoteRepository.saveAll(newNotes) // 변경사항 저장

        return "메모가 업데이트되었습니다."
    }

    /**
     * 유저노트 저장
     */
    @Transactional
    fun saveUserNote(
        userNoteDto: UserNoteDto,
        userId: Long,
    ): String {
        // 저장된 메모 수정
        if (userNoteDto.id != null) {
            val findUserNote =
                userNoteRepository.findByIdOrNull(userNoteDto.id)
                    ?: throw InvalidInputException("존재하지 않는 회원입니다.")

            if (findUserNote.member.id == userId) {
                findUserNote.title = userNoteDto.title.toString()
                findUserNote.content = userNoteDto.content

                return "메모가 업데이트되었습니다."
            } else {
                throw InvalidInputException("잘못된 접근입니다.")
            }
        } else {
            val findMember =
                memberRepository.findByIdOrNull(userId)
                    ?: throw InvalidInputException("존재하지 않는 회원입니다.")

            userNoteRepository.save(userNoteDto.toEntity(findMember))
            return "메모가 저장되었습니다."
        }
    }

    /**
     * 유저노트 불러오기
     */
    fun getUserNote(userId: Long): List<UserNoteDto>? {
        val findMember: Member =
            memberRepository.findByIdOrNull(userId)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")
        val findMemo =
            userNoteRepository.findAllByMember(findMember)
                ?: return null
        return findMemo.map { entity -> entity.toDto() }
    }

    /**
     * 주어진 사용자 ID에 대한 노트를 삭제하고, 삭제된 후의 노트 리스트를 반환하는 함수입니다.
     *
     * @param noteId 삭제할 노트의 ID
     * @param userId 노트를 삭제할 사용자의 ID
     * @return 삭제된 후의 노트 리스트 (UserNoteDto 형태로 반환)
     * @throws InvalidInputException 해당 노트가 없거나, 삭제할 수 없는 노트일 경우 예외 발생
     */
    @Transactional
    fun deleteUserNote(
        noteId: Long,
        userId: Long,
    ): List<UserNoteDto>? {
        val findMember: Member =
            memberRepository.findByIdOrNull(userId)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")

        val findUserNote =
            userNoteRepository.findAllByMember(findMember)
                ?: throw InvalidInputException("회원의 유저노트가 없습니다.")

        // 삭제할 노트 찾기
        val noteToDelete =
            findUserNote.find { it.id == noteId }
                ?: throw InvalidInputException("해당 노트를 찾을 수 없습니다.")

        // 노트 삭제
        userNoteRepository.delete(noteToDelete)

        // 삭제된 노트를 제외한 나머지 노트 리스트 반환
        val updatedNotes = findUserNote.filter { it.id != noteId }

        return updatedNotes.map { entity -> entity.toDto() }
    }
}
