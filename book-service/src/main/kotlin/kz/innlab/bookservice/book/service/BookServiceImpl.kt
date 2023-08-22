package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Books
import kz.innlab.bookservice.book.repository.BookRepository
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.containsName
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.deletedAtIsNull
import kz.innlab.bookservice.system.service.PermissionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class BookServiceImpl: BookService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: BookRepository

    @Autowired
    lateinit var authorService: AuthorService

    @Autowired
    lateinit var permissionService: PermissionService

    override fun getBookList(params: MutableMap<String, String>, pageR: PageRequest): Page<Books> {
//        if (!permissionService.permission(userId, "book-list", "read")) {
//            return Page.empty()
//        }
        val filterName = params["name"] ?: ""

        return repository.findAll(
            deletedAtIsNull().and(containsName(filterName)), pageR
        )
    }

    override fun getBookListMy(params: MutableMap<String, String>, name: String?): List<Books> {
        return repository.findAll(
            deletedAtIsNull()
        )
    }


    override fun getBookById(id: UUID): Optional<Books> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun createBook(book: Books, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "create")) {
//            status.message = "Permission"
//            return status
//        }
        repository.save(book)
        status.status = 1
        status.message= String.format("Book: %s has been created", book.name)
        status.value = book.id
        log.info(String.format("Book: %s has been created", book.name))
        return status
    }

    override fun editBook(newBook: Books, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "update")) {
//            status.message = "Permission"
//            return status
//        }
        repository.findByIdAndDeletedAtIsNull(newBook.id!!).ifPresentOrElse({
            it.name = newBook.name
            it.description = newBook.description
            it.avatarId = newBook.avatarId
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

    override fun deleteBook(id: UUID, userId: String): Status {
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

//    fun setAuthorsToBooks(books: List<Book>){
//        var authorIds = setOf<UUID>()
//        for (book in books) {
//            authorIds = authorIds.plus(book.authorIds!!)
//        }
//        val allAuthors: Map<UUID, Author> = authorService.getAuthorsInList(authorIds.toList())
//            .associateBy { it.id!! }
//
//        for (book in books) {
//            for (authorId in book.authorIds!!) {
//                if (allAuthors[authorId] != null) {
//                    book.authors = book.authors.plus(allAuthors[authorId]!!)
//                }
//            }
//        }
//    }

}
