package kz.innlab.bookservice.videolecture.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.videolecture.model.BookVideoLecture
import java.util.*

interface BookVideoLectureService {
    fun getBookVideoLectureByBookId(bookId: UUID, name: String): List<BookVideoLecture>
    fun getVideoLectureById(id: UUID, name: String?): Optional<BookVideoLecture>
    fun createBookVideoLecture(book: BookVideoLecture, name: String): Status
    fun editBookVideoLecture(book: BookVideoLecture, name: String): Status
    fun deleteBookVideoLecture(id: UUID, name: String): Status
}
