package kz.innlab.bookservice.test.controller

import kz.innlab.bookservice.test.model.BookTest
import kz.innlab.bookservice.test.service.BookTestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("test")
class BookTestController {
    @Autowired
    lateinit var service: BookTestService

    @GetMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    fun getBookTestByBookId(@PathVariable bookId: UUID, principal: Principal): List<BookTest> {
        return service.getBookTestByBookId(bookId, principal.name)
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getBookTestById(@PathVariable id: UUID, principal: Principal): Optional<BookTest> {
        return service.getBookTestById(id, principal.name)
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    fun createBook(@RequestBody book: BookTest, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.createBookTest(book, principal.name), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun editBook(@PathVariable id: UUID, @RequestBody book: BookTest, principal: Principal): ResponseEntity<*>{
        book.id = id
        return ResponseEntity(service.editBookTest(book, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteBook(@PathVariable id: UUID, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.deleteBookTest(id, principal.name), HttpStatus.OK)
    }
}
