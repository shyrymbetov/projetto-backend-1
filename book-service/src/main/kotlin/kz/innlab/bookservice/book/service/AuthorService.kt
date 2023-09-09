package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Author
import java.util.UUID

interface AuthorService {
    fun getAuthorsByBookId(bookId: UUID): List<Author>
    fun getBookIdsByAuthor(name: String): List<UUID>

    fun createAuthors(bookId: UUID, authors: List<Author>): Status
    fun editAuthors(authors: List<Author>, bookId: UUID): Status
    fun deleteAuthorsByBookId(bookId: UUID): Status

}

