package kz.innlab.bookservice.book.repository

import kz.innlab.bookservice.book.model.Author
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AuthorRepository: JpaRepository<Author, UUID> {
    fun findAllByBookIdAndDeletedAtIsNull(bookId: UUID): List<Author>
    fun findAllByIdInAndDeletedAtIsNull(ids: List<UUID>): List<Author>
    fun findAllByUserIdAndDeletedAtIsNull(userId: UUID): List<Author>
}
