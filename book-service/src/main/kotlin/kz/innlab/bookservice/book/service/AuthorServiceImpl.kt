package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Author
import kz.innlab.bookservice.book.repository.AuthorRepository
import kz.innlab.bookservice.book.repository.AuthorSpecification.Companion.containsFirstName
import kz.innlab.bookservice.book.repository.AuthorSpecification.Companion.containsLastName
import kz.innlab.bookservice.book.repository.AuthorSpecification.Companion.deletedAtIsNull
import kz.innlab.bookservice.system.service.PermissionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.RequestParam
import java.sql.Timestamp
import java.util.*

@Service
class AuthorServiceImpl: AuthorService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: AuthorRepository

    @Autowired
    lateinit var permissionService: PermissionService

    override fun getListAuthors(userId: String): List<Author> {
        if (!permissionService.permission(userId, "authors", "read")) {
            return listOf()
        }
        val result = repository.findAllByDeletedAtIsNull()
        setNumberBooksToAuthors(result)
        return result
    }

    override fun getPageAuthors(params: MutableMap<String, String>, pageR: PageRequest, userId: String): Page<Author> {
        if (!permissionService.permission(userId, "authors", "read")) {
            return Page.empty()
        }
        val filteredFirstName = params["firstname"] ?: ""
        val filteredLastName = params["lastname"] ?: ""
        val result = repository.findAll(
            containsFirstName(filteredFirstName)
                .and(containsLastName(filteredLastName))
                .and(deletedAtIsNull())
            , pageR
        )
        setNumberBooksToAuthors(result.content)
        return result
    }

    override fun getAuthorById(id: UUID, userId: String): Optional<Author> {
        if (!permissionService.permission(userId, "authors", "read")) {
            return Optional.empty()
        }
        val authorCandidate = repository.findByIdAndDeletedAtIsNull(id)
        if (authorCandidate.isPresent) {
            setNumberBooksToAuthors(listOf(authorCandidate.get()))
            return authorCandidate
        }
        return Optional.empty()
    }

    override fun createAuthor(author: Author, userId: String): Status {
        val status = Status()
        if (!permissionService.permission(userId, "authors", "create")) {
            status.message = "Permission"
            return status
        }
        repository.save(author)

        status.status = 1
        status.message = String.format("New author has been created: %s", author.firstName)
        status.value = author
        log.info("New author has been created: " + author.firstName)
        return status
    }

    override fun editAuthor(author: Author, userId: String): Status {
        val status = Status()
        if (author.id == null) {
            status.message = "Author can not null"
            return status
        }
        if (!permissionService.permission(userId, "authors", "update")) {
            status.message = "Permission"
            return status
        }

        val optAuthor = repository.findByIdAndDeletedAtIsNull(author.id!!)
        status.message = String.format("Author with id: %s does not exists", author.id.toString())
        optAuthor.ifPresent {
            it.firstName = author.firstName
            it.lastName = author.lastName
            it.middleName = author.middleName
            repository.save(it)
            status.status = 1
            status.message = String.format("Author with id: %s has been changed", author.id.toString())
            status.value = it
            log.debug("Author ${author.firstName} has been changed")
        }
        return status
    }

    override fun deleteAuthor(id: UUID, userId: String): Status {
        val status = Status()
        if (!permissionService.permission(userId, "authors", "delete")) {
            status.message = "Permission"
            return status
        }
        status.message = String.format("Author with id: %s does not exists", id.toString())
        val optAuthor = repository.findByIdAndDeletedAtIsNull(id)
        optAuthor.ifPresent {
            it.deletedAt = Timestamp(System.currentTimeMillis())
            repository.save(it)
            status.status = 1
            status.message = String.format("Author with id: %s has been deleted", id.toString())
            status.value = it.id
            log.debug("Author ${it.firstName} has been deleted")
        }
        return status
    }

    override fun getAuthorsInList(authorIds: List<UUID>, userId: String): List<Author> {
        if (!permissionService.permission(userId, "authors", "read")) {
            return listOf()
        }
        return getAuthorsInList(authorIds)
    }

    override fun getAuthorsInList(authorIds: List<UUID>): List<Author> {
        return repository.findByIdInAndDeletedAtIsNull(authorIds)
    }

    private fun setNumberBooksToAuthors(authors: List<Author>) {
        val authorNumberBooks = repository.getAuthorsByDeletedAtIsNull(authors.map { it.id!! }.toList()).associate {
            UUID.fromString(it["id"]) to Integer.parseInt(it["count"])
        }
        for (author in authors) {
            if (authorNumberBooks.containsKey(author.id)) {
                author.numberBooks = authorNumberBooks[author.id]!!
            }
        }
    }
}
