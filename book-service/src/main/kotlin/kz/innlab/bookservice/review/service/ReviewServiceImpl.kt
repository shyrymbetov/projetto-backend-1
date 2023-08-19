package kz.innlab.bookservice.review.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.review.model.Review
import kz.innlab.bookservice.review.repository.ReviewRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class ReviewServiceImpl : ReviewService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: ReviewRepository


    override fun getReviewById(id: UUID): Optional<Review> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun getReviewList(): List<Review> {
        return repository.findAllByDeletedAtIsNull()
    }

    override fun createReview(review: Review): Status {
        val status = Status()
        repository.save(review)
        status.status = 1
        status.message = String.format("Review: %s has been created", review.title)
        status.value = review.id
        log.info(String.format("Review: %s has been created", review.title))
        return status
    }

    override fun editReview(review: Review): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(review.id!!)
            .ifPresentOrElse(
                {
                    it.comment = review.comment
                    it.title = review.title
                    it.files = review.files
                    repository.save(it)
                    status.status = 1
                    status.message = String.format("Review: %s has been edited", review.title)
                    status.value = review.id
                    log.info(String.format("Review: %s has been edited", review.title))
                },
                {
                    status.message = String.format("Review: %s does not exist", review.id)
                    log.info(String.format("Review: %s does not exist", review.id))
                }
            )
        return status
    }

    override fun deleteReview(id: UUID): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresentOrElse(
                {
                    it.deletedAt = Timestamp(System.currentTimeMillis())
                    repository.save(it)
                    status.status = 1
                    status.message = String.format("Review: %s has been deleted", id)
                    log.info(String.format("Review: %s has been deleted", id))
                },
                {
                    status.message = String.format("Review: %s does not exist", id)
                    log.info(String.format("Review: %s does not exist", id))
                }
            )
        return status
    }
}
