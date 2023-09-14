package kz.innlab.bookservice.content.repository

import kz.innlab.bookservice.content.model.BookAbstracts
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Timestamp
import java.util.*

interface BookAbstractsRepository: JpaRepository<BookAbstracts, UUID>{
    fun findByUserIdAndBookIdAndDeletedAtIsNull(userId: UUID, bookId: UUID): List<BookAbstracts>
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<BookAbstracts>
}
