package kz.innlab.bookservice.hyperlink.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Books
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.deletedAtIsNull
import kz.innlab.bookservice.hyperlink.model.BookHyperlink
import kz.innlab.bookservice.hyperlink.repository.BookHyperlinkRepository
import kz.innlab.bookservice.system.service.PermissionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class BookHyperlinkServiceImpl: BookHyperlinkService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: BookHyperlinkRepository

    @Autowired
    lateinit var permissionService: PermissionService

    override fun getLinksByBookId(bookId: UUID, name: String): List<BookHyperlink> {
        return repository.findByBookIdAndDeletedAtIsNull(bookId)
    }

    override fun createHyperlink(book: BookHyperlink, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "create")) {
//            status.message = "Permission"
//            return status
//        }
        repository.save(book)
        status.status = 1
        status.message= String.format("Book: %s has been created", book.link)
        status.value = book.id
        log.info(String.format("Book: %s has been created", book.link))
        return status
    }

    override fun editHyperlink(newBook: BookHyperlink, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "update")) {
//            status.message = "Permission"
//            return status
//        }
        repository.findByIdAndDeletedAtIsNull(newBook.id!!).ifPresentOrElse({
            it.link = newBook.link
            repository.save(it)
            status.status = 1
            status.message = String.format("Book %s has been edited", it.id)
            log.info(String.format("Book %s has been edited", it.id))
            status.value = it.id
        },{

            status.message = String.format("Book %s does not exist", newBook.id)
            log.info(String.format("Book %s does not exist", newBook.id))
        })

        return status
    }

    override fun deleteHyperlink(id: UUID, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "delete")) {
//            status.message = "Permission"
//            return status
//        }
        status.message = String.format("Book %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent {
                it.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(it)
                status.status = 1
                status.message = String.format("Book %s has been deleted", id)
                log.info(String.format("Book %s has been deleted", id))
            }
        return status
    }


}
