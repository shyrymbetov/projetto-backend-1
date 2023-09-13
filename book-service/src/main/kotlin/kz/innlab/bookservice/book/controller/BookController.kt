package kz.innlab.bookservice.book.controller

import kz.innlab.bookservice.book.model.Books
import kz.innlab.bookservice.book.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.awt.print.Book
import java.security.Principal
import java.util.*

@RestController
class BookController {
    @Autowired
    lateinit var service: BookService

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getBookList(
        @RequestParam(value = "page") page: Int? = 1,
        @RequestParam(value = "size") size: Int? = 20,
        @RequestParam params: MutableMap<String, String> = mutableMapOf(),
        principal: Principal
    ): Page<Books> {
        val pageR: PageRequest = PageRequest.of((page ?: 1) - 1, (size ?: 20), Sort.by(Sort.Direction.ASC, "createdAt"))
        return service.getBookList(params, pageR)
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    fun getBookList(
        @RequestParam params: MutableMap<String, String> = mutableMapOf(),
        principal: Principal
    ): List<Books> {
        return service.getBookListMy(params, principal.name)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getBookById(@PathVariable id: UUID, principal: Principal): Optional<Books>{
        return service.getBookById(id)
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    fun createBook(@RequestBody book: Books, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.createBook(book, principal.name), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun editBook(@PathVariable id: UUID, @RequestBody book: Books, principal: Principal): ResponseEntity<*>{
        book.id = id
        return ResponseEntity(service.editBook(book, principal.name), HttpStatus.OK)
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("isAuthenticated()")
    fun editStatusBook(@PathVariable id: UUID, @RequestBody book: Books, principal: Principal): ResponseEntity<*>{
        book.id = id
        return ResponseEntity(service.editStatusBook(book, principal.name), HttpStatus.OK)
    }

    @PostMapping("/status")
    @PreAuthorize("isAuthenticated()")
    fun editStatusBook(principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.editStatusAllBooks(principal.name), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteBook(@PathVariable id: UUID, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.deleteBook(id, principal.name), HttpStatus.OK)
    }
}
