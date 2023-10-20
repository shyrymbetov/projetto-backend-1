package kz.innlab.bookservice.test.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.test.model.BookTest
import kz.innlab.bookservice.test.model.BookTestUser
import java.util.*

interface BookTestService {
    fun getBookTestById(id: UUID, name: String): Optional<BookTest>
    fun getBookTestByBookId(bookId: UUID, name: String): List<BookTest>
    fun getBookTestProgressByBookId(bookId: UUID, testUserId: UUID): Any
    fun createBookTest(book: BookTest, name: String): Status
    fun editBookTest(book: BookTest, name: String): Status
    fun deleteBookTest(id: UUID, name: String): Status
    fun completeBookTest(testUser: BookTestUser, name: String): Status
}
