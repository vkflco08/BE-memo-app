package com.memo.memo.dailymemo.service

import com.memo.memo.common.exception.exceptions.InvalidInputException
import com.memo.memo.common.exception.exceptions.MemoNotFoundException
import com.memo.memo.common.exception.exceptions.UserNotFoundException
import com.memo.memo.dailymemo.dto.ContentDtoRequest
import com.memo.memo.dailymemo.dto.ContentDtoResponse
import com.memo.memo.dailymemo.entity.DailyMemo
import com.memo.memo.dailymemo.repository.DailyMemoRepository
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
class DailyMemoService(
    private val dailyMemoRepository: DailyMemoRepository,
    private val memberRepository: MemberRepository,
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
        val existingDailyMemo: DailyMemo? = dailyMemoRepository.findByMemberAndDate(findMember, contentDtoRequest.date)

        if (existingDailyMemo != null) {
            // 메모가 존재하면 업데이트
            existingDailyMemo.title = contentDtoRequest.title
            existingDailyMemo.content = contentDtoRequest.content
            dailyMemoRepository.save(existingDailyMemo) // 변경사항 저장
            return "메모가 업데이트되었습니다."
        } else {
            // 메모가 존재하지 않으면 새로운 메모 저장
            val saveDailyMemo: DailyMemo = contentDtoRequest.toEntity(findMember)
            dailyMemoRepository.save(saveDailyMemo)
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
        val findMemos: Page<DailyMemo> = dailyMemoRepository.findAllByMember(findMember, pageable)

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
        val findMemos: List<DailyMemo> = dailyMemoRepository.findAllByMemberAndYearMonth(findMember, yearMonth)

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
            dailyMemoRepository.findByMemberAndDate(findMember, date)
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
        dailyMemoRepository.deleteByMemberAndDate(findMember, date)
        return "메모가 정상적으로 삭제되었습니다."
    }

    /**
     * 메모 수정하기
     */
    @Transactional
    fun editMemo(contentDtoRequest: ContentDtoRequest): String {
        val findMember =
            memberRepository.findByIdOrNull(contentDtoRequest.id)
                ?: throw UserNotFoundException()
        val existingContent = dailyMemoRepository.findByMemberAndDate(findMember, contentDtoRequest.date)

        return if (existingContent != null) {
            // 수정할 메모가 존재할 경우
            existingContent.title = contentDtoRequest.title
            existingContent.content = contentDtoRequest.content
            dailyMemoRepository.save(existingContent) // 변경된 내용 저장
            return "수정이 완료되었습니다."
        } else {
            throw MemoNotFoundException()
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
                ?: throw UserNotFoundException()

        val pageable: Pageable = PageRequest.of(page, size, Sort.by("date").descending())
        val contents = dailyMemoRepository.searchByMemberAndKeyword(findMember, keyword, pageable)

        // ContentDtoResponse 형태로 변환하여 반환
        return contents.map { it.toDto() }
    }
}
