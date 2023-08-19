package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Author
import kz.innlab.bookservice.book.model.Book
import kz.innlab.bookservice.book.model.payload.BookLevel
import kz.innlab.bookservice.book.repository.BookRepository
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.containsAuthorId
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.containsGenreId
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.containsName
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.deletedAtIsNull
import kz.innlab.bookservice.book.repository.BookSpecification.Companion.equalsLevel
import kz.innlab.bookservice.system.service.PermissionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.util.Assert
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
    lateinit var genreService: GenreService

    @Autowired
    lateinit var permissionService: PermissionService

    override fun getBookList(params: MutableMap<String, String>, pageR: PageRequest, userId: String): Page<Book> {
        if (!permissionService.permission(userId, "book-list", "read")) {
            return Page.empty()
        }
        val filterName = params["name"] ?: ""
        val filterGenreId: UUID? = if (params["genreId"] != null) UUID.fromString(params["genreId"]) else null
        val filterAuthorId: UUID? = if (params["authorId"] != null) UUID.fromString(params["authorId"]) else null
        val filterLevel: BookLevel? = params["level"]?.let { BookLevel.valueOf(it.uppercase()) }

        val result = repository.findAll(
            containsName(filterName)
                .and(deletedAtIsNull())
                .and(equalsLevel(filterLevel))
                .and(containsGenreId(filterGenreId))
                .and(containsAuthorId(filterAuthorId))
            , pageR)
        setAuthorsToBooks(result.content)
        setGenresToBooks(result.content)
        return result
    }

    override fun getBookById(id: UUID, userId: String): Optional<Book> {
        if (!permissionService.permission(userId, "book-list", "read")) {
            return Optional.empty()
        }
        return getBookById(id)
    }

    override fun getBookById(id: UUID): Optional<Book> {
        val bookCandidate = repository.findByIdAndDeletedAtIsNull(id)
        if (bookCandidate.isPresent) {
            val book = bookCandidate.get()
            book.authors = authorService.getAuthorsInList(book.authorIds!!.toList())
            book.genres = genreService.getGenresInList(book.genreIds!!.toList())
            return Optional.of(book)
        }
        return Optional.empty()
    }

    override fun createBook(book: Book, userId: String): Status {
        val status = Status()
        if (!permissionService.permission(userId, "book-list",  "create")) {
            status.message = "Permission"
            return status
        }
        repository.save(book)
        status.status = 1
        status.message= String.format("Book: %s has been created", book.name)
        status.value = book.id
        log.info(String.format("Book: %s has been created", book.name))
        return status
    }

    override fun editBook(newBook: Book, userId: String): Status {
        val status = Status()
        if (!permissionService.permission(userId, "book-list",  "update")) {
            status.message = "Permission"
            return status
        }
        Assert.notNull(newBook.id, "ID can't null")
        if (newBook.id != null) {
            val bookCandidate = repository.findByIdAndDeletedAtIsNull(newBook.id!!)
            if (bookCandidate.isPresent) {
                var book = bookCandidate.get()
                book.name = newBook.name
                book.authorIds = newBook.authorIds
                book.genreIds = newBook.genreIds
                book.description = newBook.description
                book.avatarId = newBook.avatarId
                book.level = newBook.level
                book.pages = newBook.pages
                repository.save(book)
                status.status = 1
                status.message = String.format("Book %s has been edited", book.id)
                log.info(String.format("Book %s has been edited", book.id))
                status.value = book.id
            } else {
                status.message = String.format("Book %s does not exist", newBook.id)
                log.info(String.format("Book %s does not exist", newBook.id))
            }
        }
        return status
    }

    override fun deleteBook(id: UUID, userId: String): Status {
        val status = Status()
        if (!permissionService.permission(userId, "book-list",  "delete")) {
            status.message = "Permission"
            return status
        }
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

    override fun getBookListByGenreAndLevel(genreId: UUID, level: String?, userId: String): List<Book> {
        if (!permissionService.permission(userId, "book-list", "read")) {
            return listOf()
        }
        return getBookListByGenreAndLevel(genreId, level)
    }

    override fun getBookListByGenreAndLevel(genreId: UUID, level: String?): List<Book> {
        val bookLevel = if (level == null) "FIRST" else level
        val result = repository.getAllByGenreIdAndBookLevelAndDeletedAtIsNull(genreId, bookLevel)
        setAuthorsToBooks(result)
        setGenresToBooks(result)
        return result
    }

    override fun getBooksByIds(bookIds: List<UUID>, userId: String): List<Book> {
        if (!permissionService.permission(userId, "book-list", "read")) {
            return listOf()
        }
        return getBooksByIds(bookIds)
    }

    override fun getBooksByIds(bookIds: List<UUID>): List<Book> {
        val result = repository.findAllByIdInAndDeletedAtIsNull(bookIds)
        setAuthorsToBooks(result)
        setGenresToBooks(result)
        return result
    }

    fun setAuthorsToBooks(books: List<Book>){
        var authorIds = setOf<UUID>()
        for (book in books) {
            authorIds = authorIds.plus(book.authorIds!!)
        }
        val allAuthors: Map<UUID, Author> = authorService.getAuthorsInList(authorIds.toList())
            .associateBy { it.id!! }

        for (book in books) {
            for (authorId in book.authorIds!!) {
                if (allAuthors[authorId] != null) {
                    book.authors = book.authors.plus(allAuthors[authorId]!!)
                }
            }
        }
    }

    fun setGenresToBooks(books: List<Book>){
        var genreIds = setOf<UUID>()
        for (book in books) {
            genreIds = genreIds.plus(book.genreIds!!)
        }
        val allGenres = genreService.getGenresInList(genreIds.toList())
            .map{ it.id!! to it }.toMap()

        for (book in books) {
            for (genreId in book.genreIds!!) {
                if (allGenres[genreId] != null) {
                    book.genres = book.genres.plus(allGenres[genreId]!!)
                }
            }
        }
    }
}
