package kz.innlab.bookservice.book.repository

import kz.innlab.bookservice.book.model.Author
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface AuthorRepository: JpaRepository<Author, UUID>, JpaSpecificationExecutor<Author> {
    fun findAllByDeletedAtIsNull(): List<Author>
    @Query("SELECT * FROM BOOK_AUTHORS WHERE (LOWER(first_name) LIKE LOWER(CONCAT('%',:search, '%')) " +
            "OR LOWER(middle_name) LIKE LOWER(CONCAT('%',:search, '%')) OR LOWER(last_name) LIKE LOWER(CONCAT('%',:search, '%'))) " +
            "AND deleted_at IS NULL", nativeQuery = true)
    fun getPageAuthors(@Param("search") search: String, page: Pageable): Page<Author>
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Author>

    fun findByIdInAndDeletedAtIsNull(ids: List<UUID>): List<Author>

    @Query(
        "SELECT CAST(author.id as varchar), CAST((SELECT COUNT(*) FROM books as b " +
                "WHERE author.id = ANY(b.author_ids) AND b.deleted_at IS NULL) as varchar) " +
                "from book_authors as author " +
                "WHERE author.deleted_at IS NULL AND author.id in :authorIds", nativeQuery = true
    )
    fun getAuthorsByDeletedAtIsNull(authorIds: List<UUID>): MutableList<MutableMap<String, String>>
}
