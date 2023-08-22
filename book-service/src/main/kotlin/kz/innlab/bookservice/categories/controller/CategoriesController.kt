package kz.innlab.bookservice.categories.controller

import kz.innlab.bookservice.categories.model.BookCategory
import kz.innlab.bookservice.categories.service.BookCategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/category")
class CategoriesController {
    @Autowired
    lateinit var service: BookCategoryService

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    fun getListBookCategory(): List<BookCategory>{
        return service.getListBookCategory()
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getBookCategoryById(@PathVariable("id") id: UUID): Optional<BookCategory> {
        return service.getBookCategoryById(id)
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    fun createBookCategory(@Valid @RequestBody category: BookCategory): ResponseEntity<*> {
        return ResponseEntity(service.createBookCategory(category), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun editBookCategory(@PathVariable id: UUID, @Valid @RequestBody category: BookCategory): ResponseEntity<*> {
        category.id = id
        return ResponseEntity(service.editBookCategory(category), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteBookCategory(@PathVariable id: UUID): ResponseEntity<*> {
        return ResponseEntity(service.deleteBookCategory(id), HttpStatus.OK)
    }
}
