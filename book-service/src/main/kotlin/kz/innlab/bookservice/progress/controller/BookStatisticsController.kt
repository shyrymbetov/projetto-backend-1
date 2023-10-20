package kz.innlab.bookservice.progress.controller

import kz.innlab.bookservice.progress.model.BookProgress
import kz.innlab.bookservice.progress.model.BookStatistics
import kz.innlab.bookservice.progress.service.BookProgressService
import kz.innlab.bookservice.progress.service.BookStatisticsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("statistics")
class BookStatisticsController {
    @Autowired
    lateinit var service: BookStatisticsService

    @Autowired
    lateinit var progressService: BookProgressService

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getBookStatisticsByBookId(
        @PathVariable id: UUID,
        principal: Principal
    ): Optional<BookStatistics> {
        return service.getBookStatisticsByBookIdAndReaderId(id, UUID.fromString(principal.name))
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    fun createBookStatistics(
        @RequestBody bookStatistics: BookStatistics,
        principal: Principal
    ): ResponseEntity<*>{
        return ResponseEntity(service.createBookStatistics(bookStatistics, UUID.fromString(principal.name)), HttpStatus.OK)
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteBookStatistics(@PathVariable id: UUID, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.deleteBookStatisticsByBookIdAndReaderId(id, UUID.fromString(principal.name)), HttpStatus.OK)
    }

    // Add an endpoint for creating BookProgress under a specific BookStatistics
    @PostMapping("/{bookId}/progress")
    @PreAuthorize("isAuthenticated()")
    fun createBookProgress(
        @PathVariable bookId: UUID,
        @RequestBody bookProgress: BookProgress,
        principal: Principal
    ): ResponseEntity<*> {
        return ResponseEntity(progressService.createBookProgress(bookId, bookProgress, UUID.fromString(principal.name)), HttpStatus.OK)
    }

}
