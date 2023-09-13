package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.BookStatusEnum
import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Books
import kz.innlab.bookservice.book.repository.AuthorRepository
import kz.innlab.bookservice.book.repository.BookRepository
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.author
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.bookIdIn
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.bookStatusPublic
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.categoryEquals
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
class BookServiceImpl : BookService {
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

        return repository.findAll(
            deletedAtIsNull()
                .and(categoryEquals(params["category"] ?: ""))
                .and(containsName(params["name"] ?: ""))
                .and(bookStatusPublic())
            , pageR
        )
    }

    override fun getBookListMy(params: MutableMap<String, String>, name: String): List<Books> {
        var condition = deletedAtIsNull().and(containsName(params["name"] ?: ""))
        if (params["list"] == "coauthor") {
            val bookIds = authorService.getBookIdsByAuthor(name)
            condition = condition.and(bookIdIn(bookIds))
        } else {
            condition = condition.and(author(UUID.fromString(name)))
        }
        return repository.findAll(condition)
    }


    override fun getBookById(id: UUID): Optional<Books> {
        val book = repository.findByIdAndDeletedAtIsNull(id)
        book.ifPresent {
            it.coauthors = authorService.getAuthorsByBookId(id)
        }
        return book
    }

    override fun createBook(book: Books, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "create")) {
//            status.message = "Permission"
//            return status
//        }
        book.status = BookStatusEnum.NOT_PUBLIC
        repository.save(book)
        authorService.createAuthors(book.id!!, book.coauthors)

        status.status = 1
        status.message = String.format("Book: %s has been created", book.name)
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

            authorService.editAuthors(newBook.coauthors, newBook.id!!)

            it.name = newBook.name
            it.description = newBook.description
            it.avatarId = newBook.avatarId
            repository.save(it)
            status.status = 1
            status.message = String.format("Book %s has been edited", it.id)
            log.info(String.format("Book %s has been edited", it.id))
            status.value = it.id
        }, {

            status.message = String.format("Book %s does not exist", newBook.id)
            log.info(String.format("Book %s does not exist", newBook.id))
        })

        return status
    }

    override fun editStatusBook(book: Books, name: String): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(book.id!!).ifPresentOrElse({
            it.status = book.status
            repository.save(it)
            status.status = 1
            status.message = String.format("Book %s has been edited", it.id)
            log.info(String.format("Book %s has been edited", it.id))
            status.value = it.id
        }, {
            status.message = String.format("Book %s does not exist", book.id)
            log.info(String.format("Book %s does not exist", book.id))
        })
        return status
    }

    override fun editStatusAllBooks(name: String): Status {
        repository.findAllByDeletedAtIsNull().forEach { book ->
            book.status = BookStatusEnum.PUBLIC
            repository.save(book)
        }
        return Status(1, "Success")
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
                authorService.deleteAuthorsByBookId(id)

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
