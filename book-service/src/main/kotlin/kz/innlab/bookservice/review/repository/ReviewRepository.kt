package kz.innlab.bookservice.review.repository

import kz.innlab.bookservice.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReviewRepository: JpaRepository<Review, UUID> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<Review>
    fun findAllByDeletedAtIsNull(): List<Review>
}
