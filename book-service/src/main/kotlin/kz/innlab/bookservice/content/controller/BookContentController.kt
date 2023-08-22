package kz.innlab.bookservice.content.controller

import kz.innlab.bookservice.book.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.awt.print.Book
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("content")
class BookContentController {
    @Autowired
    lateinit var service: BookService

//    @PostMapping("/{bookId}")
//    @PreAuthorize("isAuthenticated()")
//    fun createBook(@PathVariable bookId: UUID, @RequestBody book: Book, principal: Principal): ResponseEntity<*>{
//        return ResponseEntity(service.createBook(book, principal.name), HttpStatus.OK)
//    }

}
