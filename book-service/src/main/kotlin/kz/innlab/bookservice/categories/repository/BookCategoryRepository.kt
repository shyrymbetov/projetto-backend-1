package kz.innlab.bookservice.categories.repository

import kz.innlab.bookservice.categories.model.BookCategory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookCategoryRepository: JpaRepository<BookCategory, UUID> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<BookCategory>
    fun findAllByDeletedAtIsNull(): List<BookCategory>
    fun findByIdInAndDeletedAtIsNull(ids: List<UUID>): List<BookCategory>
}
