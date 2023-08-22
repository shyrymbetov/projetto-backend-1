package kz.innlab.bookservice.test.repository

import kz.innlab.bookservice.test.model.BookTest
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookTestRepository: JpaRepository<BookTest, UUID> {
    fun findAllByBookIdAndDeletedAtIsNull(bookId: UUID): List<BookTest>
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<BookTest>
}
