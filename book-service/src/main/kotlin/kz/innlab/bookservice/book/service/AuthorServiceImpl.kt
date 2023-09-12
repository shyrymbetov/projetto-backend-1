package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Author
import kz.innlab.bookservice.book.repository.AuthorRepository
import kz.innlab.bookservice.system.service.PermissionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Time
import java.sql.Timestamp
import java.util.*

@Service
class AuthorServiceImpl : AuthorService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: AuthorRepository

    @Autowired
    lateinit var permissionService: PermissionService
    override fun getAuthorsByBookId(bookId: UUID): List<Author> {
        return repository.findAllByBookIdAndDeletedAtIsNull(bookId)
    }

    override fun getBookIdsByAuthor(name: String): List<UUID> {
        return repository.findAllByUserIdAndDeletedAtIsNull(UUID.fromString(name))
            .mapNotNull { it.bookId }
    }

    override fun createAuthors(bookId: UUID, authors: List<Author>): Status {
        authors.forEach { author ->
            author.bookId = bookId
            repository.save(author)
        }
        return Status(1, "Success")
    }

    override fun editAuthors(authors: List<Author>, bookId: UUID): Status {
        val authorsOld = repository.findAllByBookIdAndDeletedAtIsNull(bookId)

        authors.forEach { author ->
            author.bookId = bookId
            repository.save(author)
        }

        val  authorsRest = authors.associateBy { it.id }
        authorsOld.forEach {
            if (!authorsRest.containsKey(it.id)) {
                it.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(it)
            }
        }

        return Status(1, "Success")
    }

    override fun deleteAuthorsByBookId(bookId: UUID): Status {
        repository.findAllByBookIdAndDeletedAtIsNull(bookId).forEach { author ->
            author.deletedAt = Timestamp(System.currentTimeMillis())
            repository.save(author)
        }
        return Status(1, "Success")
    }

}
