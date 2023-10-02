package kz.innlab.bookservice.book.repository

import kz.innlab.bookservice.book.model.Books
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface BookRepository: JpaRepository<Books, UUID>, JpaSpecificationExecutor<Books> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Books>
    fun findAllByIdInAndDeletedAtIsNull(bookIds: List<UUID>): List<Books>
    fun findAllByDeletedAtIsNull(): List<Books>
    fun findOneByFileIdAndDeletedAtIsNull(fileId: UUID): Optional<Books>
}
