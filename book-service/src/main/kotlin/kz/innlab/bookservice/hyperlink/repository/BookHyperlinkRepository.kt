package kz.innlab.bookservice.hyperlink.repository

import kz.innlab.bookservice.book.model.Books
import kz.innlab.bookservice.hyperlink.model.BookHyperlink
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface BookHyperlinkRepository: JpaRepository<BookHyperlink, UUID> {
    fun findByBookIdAndDeletedAtIsNull(bookId: UUID): List<BookHyperlink>
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<BookHyperlink>
}
