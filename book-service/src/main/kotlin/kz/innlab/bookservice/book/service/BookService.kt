package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Books
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.*

interface BookService {
    fun getBookList(params: MutableMap<String, String>, pageR: PageRequest): Page<Books>
    fun getBookListMy(params: MutableMap<String, String>, name: String): List<Books>
    fun getBookById(id: UUID): Optional<Books>
    fun createBook(book: Books, userId: String): Status
    fun editBook(book: Books, userId: String): Status
    fun deleteBook(id: UUID, userId: String): Status
}
