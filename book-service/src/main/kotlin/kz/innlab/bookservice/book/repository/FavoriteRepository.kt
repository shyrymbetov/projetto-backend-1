package kz.innlab.bookservice.book.repository

import kz.innlab.bookservice.book.model.Favorite
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FavoriteRepository: JpaRepository<Favorite, UUID> {
    fun findAllByUserIdAndDeletedAtIsNull(userId: UUID): List<Favorite>
    fun findByUserIdAndBookId(userId: UUID, bookId: UUID): Optional<Favorite>
}
