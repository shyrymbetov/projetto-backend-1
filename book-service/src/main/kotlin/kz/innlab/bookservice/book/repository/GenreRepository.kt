package kz.innlab.bookservice.book.repository

import kz.innlab.bookservice.book.model.Genre
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface GenreRepository: JpaRepository<Genre, UUID> {
    @Query("SELECT * FROM BOOK_GENRES WHERE LOWER(name) LIKE LOWER(CONCAT('%',:search, '%')) AND deleted_at IS NULL", nativeQuery = true)
    fun getPageListGenres(@Param("search") search: String, page: Pageable): Page<Genre>
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Genre>
    fun findByGenreOrderAndDeletedAtIsNull(genreOrder: Int): Optional<Genre>
    fun findAllByDeletedAtIsNull(): List<Genre>

    fun findByIdInAndDeletedAtIsNull(ids: List<UUID>): List<Genre>
}
