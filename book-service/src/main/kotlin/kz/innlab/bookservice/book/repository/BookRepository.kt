package kz.innlab.bookservice.book.repository

import kz.innlab.bookservice.book.model.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.query.Param
import java.util.*

interface BookRepository: JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Book>
    @Query("SELECT * FROM books " +
            "WHERE LOWER(name) LIKE LOWER(CONCAT('%',:search, '%')) AND deleted_at IS NULL", nativeQuery = true)
    fun getAllBySearchAndDeletedAtIsNull(@Param("search") search: String, page: Pageable): Page<Book>

//    TODO filter author
//     (cast(:genreId as org.hibernate.type.UUIDCharType) IS NULL OR :genreId = ANY(genre_ids)) AND
    @Query("SELECT * FROM books " +
            "WHERE " +
            "(:name IS NULL OR LOWER(name) LIKE LOWER(CONCAT('%',:name, '%'))) AND " +
            "(:author IS NULL OR true) AND " +
            "(:level IS NULL OR :level=level) AND deleted_at IS NULL", nativeQuery = true)
    fun getAllByFilterAndDeletedAtIsNull(name: String?, author: String?, level: String?, page: Pageable): Page<Book>

    @Query("SELECT * FROM books " +
            "WHERE :genreId = ANY(genre_ids) AND :bookLevel=level AND deleted_at IS NULL", nativeQuery = true)
    fun getAllByGenreIdAndBookLevelAndDeletedAtIsNull(genreId: UUID, bookLevel: String): List<Book>

    fun findAllByIdInAndDeletedAtIsNull(bookIds: List<UUID>): List<Book>
}
