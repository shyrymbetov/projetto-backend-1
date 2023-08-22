package kz.innlab.bookservice.hyperlink.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.hyperlink.model.BookHyperlink
import java.util.*

interface BookHyperlinkService {
    fun getLinksByBookId(bookId: UUID, name: String): List<BookHyperlink>
    fun createHyperlink(book: BookHyperlink, name: String): Status
    fun editHyperlink(book: BookHyperlink, name: String): Status
    fun deleteHyperlink(id: UUID, name: String): Status
}
