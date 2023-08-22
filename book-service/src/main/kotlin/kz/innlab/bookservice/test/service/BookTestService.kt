package kz.innlab.bookservice.test.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.hyperlink.model.BookHyperlink
import kz.innlab.bookservice.test.model.BookTest
import java.util.*

interface BookTestService {
    fun getBookTestById(id: UUID, name: String): Optional<BookTest>
    fun getBookTestByBookId(bookId: UUID, name: String): List<BookTest>
    fun createBookTest(book: BookTest, name: String): Status
    fun editBookTest(book: BookTest, name: String): Status
    fun deleteBookTest(id: UUID, name: String): Status
}
