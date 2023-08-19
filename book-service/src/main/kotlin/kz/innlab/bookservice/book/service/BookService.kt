package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.*

interface BookService {
    fun getBookList(params: MutableMap<String, String>, pageR: PageRequest, userId: String): Page<Book>
    fun getBookById(id: UUID, userId: String): Optional<Book>
    fun getBookById(id: UUID): Optional<Book>
    fun createBook(book: Book, userId: String): Status
    fun editBook(book: Book, userId: String): Status
    fun deleteBook(id: UUID, userId: String): Status
    fun getBookListByGenreAndLevel(genreId: UUID, level: String?, userId: String): List<Book>
    fun getBookListByGenreAndLevel(genreId: UUID, level: String?): List<Book>

    fun getBooksByIds(bookIds: List<UUID>, userId: String): List<Book>
    fun getBooksByIds(bookIds: List<UUID>): List<Book>
}
