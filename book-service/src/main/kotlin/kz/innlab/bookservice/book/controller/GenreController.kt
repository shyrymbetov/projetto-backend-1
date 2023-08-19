package kz.innlab.bookservice.book.controller

import kz.innlab.bookservice.book.model.Genre
import kz.innlab.bookservice.book.service.GenreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/genre")
class GenreController {
    @Autowired
    lateinit var service: GenreService

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getListGenre(): List<Genre>{
        return service.getListGenre();
    }

    @GetMapping("/list/page")
    @PreAuthorize("isAuthenticated()")
    fun getPageListGenre(
        @RequestParam(value = "page") page: Int? = 1,
        @RequestParam(value = "size") size: Int? = 20,
        @RequestParam(value = "search") search: String? =  null
    ): Page<Genre> {
        val pageR: PageRequest = PageRequest.of((page ?: 1) - 1, (size ?: 20), Sort.by(Sort.Direction.ASC, "created_at"))
        return service.getPageListGenre(pageR, search ?: "")
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getGenreById(@PathVariable("id") id: UUID): Optional<Genre> {
        return service.getGenreById(id)
    }

    @GetMapping("/order/{genreOrder}")
    @PreAuthorize("isAuthenticated()")
    fun getGenreByGenreOrder(@PathVariable genreOrder: Int): Optional<Genre>{
        return service.getGenreByGenreOrder(genreOrder)
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    fun createGenre(@Valid @RequestBody genre: Genre): ResponseEntity<*> {
        return ResponseEntity(service.createGenre(genre), HttpStatus.OK)
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    fun editGenre(@Valid @RequestBody genre: Genre): ResponseEntity<*> {
        return ResponseEntity(service.editGenre(genre), HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteGenre(@PathVariable id: UUID): ResponseEntity<*> {
        return ResponseEntity(service.deleteGenre(id), HttpStatus.OK)
    }
}
