package kz.innlab.bookservice.content.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Books
import kz.innlab.bookservice.content.model.BookAbstracts
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.*

interface BookAbstractService {
    fun getBookAbstractList(bookId: UUID, username: String): List<BookAbstracts>
    fun createBookAbstract(bookAbstract: BookAbstracts, username: String): Status
    fun deleteBookAbstract(id: UUID, name: String): Status
}
