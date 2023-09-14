package kz.innlab.bookservice.content.controller

import kz.innlab.bookservice.content.model.BookAbstracts
import kz.innlab.bookservice.content.service.BookAbstractService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("abstract")
class BookAbstractController {
    @Autowired
    lateinit var service: BookAbstractService

    @GetMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    fun createBookAbstracts(@PathVariable bookId: UUID, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.getBookAbstractList(bookId, principal.name), HttpStatus.OK)
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    fun createBookAbstracts(@RequestBody bookAbstract: BookAbstracts, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.createBookAbstract(bookAbstract, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteBookAbstracts(@PathVariable id: UUID, principal: Principal): ResponseEntity<*>{
        return ResponseEntity(service.deleteBookAbstract(id, principal.name), HttpStatus.OK)
    }


}
