package kz.innlab.bookservice.review.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.review.model.Review
import java.util.*

interface ReviewService {
    fun getReviewById(id: UUID): Optional<Review>
    fun getReviewList(): List<Review>
    fun createReview(review: Review): Status
    fun editReview(review: Review): Status
    fun deleteReview(id: UUID): Status
}
