package kz.innlab.bookservice.book.controller

import kz.innlab.bookservice.book.model.Author
import kz.innlab.bookservice.book.service.AuthorService
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
import javax.validation.Valid

@RestController
@RequestMapping("/author")
class AuthorController {
    @Autowired
    lateinit var service: AuthorService


    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getListAuthors(principal: Principal): List<Author> {
        return service.getListAuthors(principal.name)
    }

    @GetMapping("/list/page")
    @PreAuthorize("isAuthenticated()")
    fun getPageAuthors(
        @RequestParam(value = "page") page: Int? = 1,
        @RequestParam(value = "size") size: Int? = 20,
        @RequestParam params: MutableMap<String, String> = mutableMapOf(),
        principal: Principal
    ): Page<Author> {
        val pageR: PageRequest = PageRequest.of((page ?: 1) - 1, (size ?: 20), Sort.by(Sort.Direction.ASC, "createdAt"))
        return service.getPageAuthors(params, pageR, principal.name)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getAuthorById(@PathVariable("id") id: UUID, principal: Principal): Optional<Author> {
        return service.getAuthorById(id, principal.name)
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createAuthor(@Valid @RequestBody author: Author, principal: Principal): ResponseEntity<*> {
        return ResponseEntity(service.createAuthor(author, principal.name), HttpStatus.OK)
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    fun editAuthor(@Valid @RequestBody author: Author, principal: Principal): ResponseEntity<*> {
        return ResponseEntity(service.editAuthor(author, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteAuthor(@PathVariable id: UUID, principal: Principal): ResponseEntity<*> {
        return ResponseEntity(service.deleteAuthor(id, principal.name), HttpStatus.OK)
    }
}
