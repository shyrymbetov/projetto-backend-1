package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Author
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

interface AuthorService {
    fun getPageAuthors(params: MutableMap<String, String>, pageR: PageRequest, userId: String): Page<Author>
    fun getAuthorById(id: UUID, userId: String): Optional<Author>
    fun createAuthor(author: Author, userId: String): Status
    fun editAuthor(author: Author, userId: String): Status
    fun deleteAuthor(id: UUID, userId: String): Status
//    fun updateAuthorBookNumber(authorId: UUID)
    fun getAuthorsInList(authorIds: List<UUID>, userId: String): List<Author>
    fun getAuthorsInList(authorIds: List<UUID>): List<Author>
    fun getListAuthors(userId: String): List<Author>
}

