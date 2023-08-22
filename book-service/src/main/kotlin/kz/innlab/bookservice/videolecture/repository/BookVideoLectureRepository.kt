package kz.innlab.bookservice.videolecture.repository

import kz.innlab.bookservice.videolecture.model.BookVideoLecture
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookVideoLectureRepository: JpaRepository<BookVideoLecture, UUID> {
    fun findAllByBookIdAndDeletedAtIsNull(bookId: UUID): List<BookVideoLecture>
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<BookVideoLecture>
}
