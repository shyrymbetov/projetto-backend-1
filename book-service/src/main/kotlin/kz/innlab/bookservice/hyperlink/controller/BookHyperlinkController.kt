package kz.innlab.bookservice.hyperlink.controller

import kz.innlab.bookservice.hyperlink.model.BookHyperlink
import kz.innlab.bookservice.hyperlink.service.BookHyperlinkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("hyperlink")
class BookHyperlinkController {
    @Autowired
    lateinit var service: BookHyperlinkService

    @GetMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    fun getBookById(@PathVariable bookId: UUID, principal: Principal): List<BookHyperlink> {
        return service.getLinksByBookId(bookId, principal.name)
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    fun createBook(@RequestBody book: BookHyperlink, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.createHyperlink(book, principal.name), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun editBook(@PathVariable id: UUID, @RequestBody book: BookHyperlink, principal: Principal): ResponseEntity<*>{
        book.id = id
        return ResponseEntity(service.editHyperlink(book, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteBook(@PathVariable id: UUID, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.deleteHyperlink(id, principal.name), HttpStatus.OK)
    }
}
