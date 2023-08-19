package kz.innlab.bookservice.book.controller

import kz.innlab.bookservice.book.model.Book
import kz.innlab.bookservice.book.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
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
    ): Page<Book> {
        val pageR: PageRequest = PageRequest.of((page ?: 1) - 1, (size ?: 20), Sort.by(Sort.Direction.ASC, "createdAt"))
        return service.getBookList(params, pageR, principal.name)
    }

    @GetMapping("/list/genre/{genreId}")
    @PreAuthorize("isAuthenticated()")
    fun getBookListByGenre(
        @PathVariable genreId: UUID,
        @RequestParam(value = "level") level: String? = "FIRST",
        principal: Principal
    ): List<Book>{
        return service.getBookListByGenreAndLevel(genreId, level, principal.name)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getBookById(@PathVariable id: UUID, principal: Principal): Optional<Book>{
        return service.getBookById(id, principal.name)
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createBook(@RequestBody book: Book, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.createBook(book, principal.name), HttpStatus.OK)
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    fun editBook(@RequestBody book: Book, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.editBook(book, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteBook(@PathVariable id: UUID, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.deleteBook(id, principal.name), HttpStatus.OK)
    }
}
