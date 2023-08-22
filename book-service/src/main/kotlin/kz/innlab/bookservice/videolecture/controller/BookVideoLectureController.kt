package kz.innlab.bookservice.videolecture.controller

import kz.innlab.bookservice.videolecture.model.BookVideoLecture
import kz.innlab.bookservice.videolecture.service.BookVideoLectureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("video")
class BookVideoLectureController {
    @Autowired
    lateinit var service: BookVideoLectureService

    @GetMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    fun getVideosByBookId(@PathVariable bookId: UUID, principal: Principal): List<BookVideoLecture> {
        return service.getBookVideoLectureByBookId(bookId, principal.name)
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getBookVideoLectureById(@PathVariable id: UUID, principal: Principal): Optional<BookVideoLecture> {
        return service.getVideoLectureById(id, principal.name)
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    fun createBook(@RequestBody book: BookVideoLecture, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.createBookVideoLecture(book, principal.name), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun editBook(@PathVariable id: UUID, @RequestBody book: BookVideoLecture, principal: Principal): ResponseEntity<*>{
        book.id = id
        return ResponseEntity(service.editBookVideoLecture(book, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteBook(@PathVariable id: UUID, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.deleteBookVideoLecture(id, principal.name), HttpStatus.OK)
    }
}
