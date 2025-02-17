package com.memo.memo.content.service

import com.memo.memo.common.exception.InvalidInputException
import com.memo.memo.content.dto.ContentDtoRequest
import com.memo.memo.content.dto.ContentDtoResponse
import com.memo.memo.content.dto.UserNoteDto
import com.memo.memo.content.entity.Content
import com.memo.memo.content.entity.UserNote
import com.memo.memo.content.repository.ContentRepository
import com.memo.memo.content.repository.UserNoteRepository
import com.memo.memo.member.entity.Member
import com.memo.memo.member.repository.MemberRepository
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Slf4j
@Service
class ContentService(
    private val contentRepository: ContentRepository,
    private val memberRepository: MemberRepository,
    private val usernoteRepository: UserNoteRepository,
) {
    /**
     * 메모 저장
     */
    @Transactional
    fun saveMemo(contentDtoRequest: ContentDtoRequest): String {
        val findMember: Member =
            memberRepository.findByIdOrNull(contentDtoRequest.memberId)
                ?: return "유저를 찾을 수 없습니다."

        // 같은 날짜의 메모를 찾기
        val existingContent: Content? = contentRepository.findByMemberAndDate(findMember, contentDtoRequest.date)

        if (existingContent != null) {
            // 메모가 존재하면 업데이트
            existingContent.title = contentDtoRequest.title
            existingContent.content = contentDtoRequest.content
            contentRepository.save(existingContent) // 변경사항 저장
            return "메모가 업데이트되었습니다."
        } else {
            // 메모가 존재하지 않으면 새로운 메모 저장
            val saveContent: Content = contentDtoRequest.toEntity(findMember)
            contentRepository.save(saveContent)
            return "메모가 저장되었습니다."
        }
    }

    /**
     * 전체 메모 불러오기 (페이징 적용)
     */
    fun getMemos(
        userId: Long,
        page: Int,
        size: Int,
    ): Page<ContentDtoResponse> {
        val findMember: Member =
            memberRepository.findByIdOrNull(userId)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")

        // PageRequest를 사용하여 페이지와 크기를 설정합니다.
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("date").descending())
        val findMemos: Page<Content> = contentRepository.findAllByMember(findMember, pageable)

        // Content를 ContentDtoResponse로 매핑하여 반환
        return findMemos.map { content ->
            ContentDtoResponse(
                title = content.title,
                content = content.content,
                date = content.date,
            )
        }
    }

    /**
     * 특정 달에 해당하는 메모 리턴
     */
    fun getMemosByMonth(
        userId: Long,
        yearMonth: String,
    ): List<ContentDtoResponse> {
        val findMember: Member =
            memberRepository.findByIdOrNull(userId)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")

        // 연월(YYYY-MM)에 맞는 메모 조회
        val findMemos: List<Content> = contentRepository.findAllByMemberAndYearMonth(findMember, yearMonth)

        return findMemos.map { content ->
            ContentDtoResponse(
                title = content.title,
                content = content.content,
                date = content.date,
            )
        }
    }

    /**
     * 특정 날짜 메모 불러오기
     */
    fun getMemo(
        userId: Long,
        date: String,
    ): ContentDtoResponse? {
        val findMember: Member =
            memberRepository.findByIdOrNull(userId)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")
        val findMemo =
            contentRepository.findByMemberAndDate(findMember, date)
                ?: return null
        return findMemo.toDto()
    }

    /**
     * 메모 삭제하기
     */
    @Transactional
    fun removeMemo(
        userId: Long,
        date: String,
    ): String {
        val findMember: Member =
            memberRepository.findByIdOrNull(userId)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")
        contentRepository.deleteByMemberAndDate(findMember, date)
        return "메모가 정상적으로 삭제되었습니다."
    }

    /**
     * 메모 수정하기
     */
    @Transactional
    fun editMemo(contentDtoRequest: ContentDtoRequest): String {
        val findMember =
            memberRepository.findByIdOrNull(contentDtoRequest.id)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")
        val existingContent = contentRepository.findByMemberAndDate(findMember, contentDtoRequest.date)

        return if (existingContent != null) {
            // 수정할 메모가 존재할 경우
            existingContent.title = contentDtoRequest.title
            existingContent.content = contentDtoRequest.content
            contentRepository.save(existingContent) // 변경된 내용 저장
            return "수정이 완료되었습니다."
        } else {
            return "수정할 메모가 존재하지 않습니다."
        }
    }

    /**
     * 메모 검색하기
     */
    fun searchContentByKeyword(
        userId: Long,
        keyword: String,
        page: Int,
        size: Int,
    ): Page<ContentDtoResponse> {
        val findMember: Member =
            memberRepository.findByIdOrNull(userId)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")

        val pageable: Pageable = PageRequest.of(page, size, Sort.by("date").descending())
        val contents = contentRepository.searchByMemberAndKeyword(findMember, keyword, pageable)

        // ContentDtoResponse 형태로 변환하여 반환
        return contents.map { it.toDto() }
    }

    // ====================================

    /**
     * 유저노트 전체 저장
     */
    @Transactional
    fun saveUsernotes(
        userNoteDto: List<UserNoteDto>,
        userId: Long,
    ): String {
        val findMember: Member =
            memberRepository.findByIdOrNull(userId)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")

        val existingNotes = usernoteRepository.findAllByMember(findMember) // 기존 노트 조회
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

        usernoteRepository.saveAll(newNotes) // 변경사항 저장

        return "메모가 업데이트되었습니다."
    }

    /**
     * 유저노트 저장
     */
    @Transactional
    fun saveUsernote(
        userNoteDto: UserNoteDto,
        userId: Long,
    ): String {
        // 저장된 메모 수정
        if (userNoteDto.id != null) {
            val findUserNote =
                usernoteRepository.findByIdOrNull(userNoteDto.id)
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

            usernoteRepository.save(userNoteDto.toEntity(findMember))
            return "메모가 저장되었습니다."
        }
    }

    /**
     * 유저노트 불러오기
     */
    fun getUsernote(userId: Long): List<UserNoteDto>? {
        val findMember: Member =
            memberRepository.findByIdOrNull(userId)
                ?: throw InvalidInputException("존재하지 않는 회원입니다.")
        val findMemo =
            usernoteRepository.findAllByMember(findMember)
                ?: return null
        return findMemo.map { entity -> entity.toDto() }
    }

    @Transactional
    fun deleteUsernote(
        noteId: Long,
        userId: Long,
    ): String {
        val findUserNote =
            usernoteRepository.findByIdOrNull(noteId)
                ?: throw InvalidInputException("존재하지 않는 메모입니다.")

        if (findUserNote.member.id == userId) {
            usernoteRepository.delete(findUserNote)
            // 삭제 후 다시 조회하여 확인
            val deletedNote = usernoteRepository.findByIdOrNull(noteId)

            return if (deletedNote == null) {
                "메모가 삭제되었습니다."
            } else {
                "메모 삭제에 실패했습니다."
            }
        } else {
            throw InvalidInputException("잘못된 접근입니다.")
        }
    }
}
