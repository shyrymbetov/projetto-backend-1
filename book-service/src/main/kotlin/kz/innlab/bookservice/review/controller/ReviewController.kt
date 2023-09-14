package kz.innlab.bookservice.review.controller

import kz.innlab.bookservice.review.model.Review
import kz.innlab.bookservice.review.service.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/comment")
class ReviewController {
    @Autowired
    lateinit var service: ReviewService

    @GetMapping("/{id}")
    @PreAuthorize("#oauth2.hasScope('server') or isAuthenticated()")
    fun getReviewById(@PathVariable id: UUID): Optional<Review> {
        return service.getReviewById(id)
    }

    @GetMapping("/list")
    @PreAuthorize("#oauth2.hasScope('server') or isAuthenticated()")
    fun getReviewList(): List<Review>{
        return service.getReviewList()
    }

    @PostMapping("")
    @PreAuthorize("#oauth2.hasScope('server') or isAuthenticated()")
    fun createReview(@RequestBody review: Review): ResponseEntity<*> {
        return ResponseEntity(service.createReview(review), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun editReview(@PathVariable id: UUID, @RequestBody review: Review): ResponseEntity<*> {
        review.id = id
        return ResponseEntity(service.editReview(review), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteReview(@PathVariable id: UUID): ResponseEntity<*> {
        return ResponseEntity(service.deleteReview(id), HttpStatus.OK)
    }
}
