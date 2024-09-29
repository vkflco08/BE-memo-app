package com.memo.memo.content.service

import com.memo.memo.common.exception.InvalidInputException
import com.memo.memo.content.dto.ContentDtoRequest
import com.memo.memo.content.dto.ContentDtoResponse
import com.memo.memo.content.entity.Content
import com.memo.memo.content.repository.ContentRepository
import com.memo.memo.member.entity.Member
import com.memo.memo.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Transactional
@Service
class ContentService(
    private val contentRepository: ContentRepository,
    private val memberRepository: MemberRepository,
) {
    /**
     * 메모 저장
     */
    @Transactional
    fun saveMemo(contentDtoRequest: ContentDtoRequest): String {
        val findMember: Member = memberRepository.findByIdOrNull(contentDtoRequest.memberId)
            ?: return "유저를 찾을 수 없습니다"

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
     * 전체 메모 불러오기
     */
    fun getMemos(userId: Long): List<ContentDtoResponse> {
        val findMember : Member = memberRepository.findByIdOrNull(userId)
            ?: throw InvalidInputException("존재하지 않는 회원입니다.")
        val findMemos : List<Content> = contentRepository.findAllByMember(findMember)

        return findMemos.map { content ->
            ContentDtoResponse(
                title = content.title,
                content = content.content,
                date = content.date
            )
        }
    }

    /**
     * 메모 불러오기
     */
    fun getMemo(userId: Long, date: String): ContentDtoResponse? {
        val findMember : Member = memberRepository.findByIdOrNull(userId)
            ?: throw InvalidInputException("존재하지 않는 회원입니다.")
        val findMemo = contentRepository.findByMemberAndDate(findMember, date)
            ?: return null
        return findMemo.toDto()
    }

    /**
     * 메모 수정하기
     */
    fun editMemo(contentDtoRequest: ContentDtoRequest): String {
        val findMember = memberRepository.findByIdOrNull(contentDtoRequest.id)
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
}
