package kz.innlab.userservice.user.controller

import kz.innlab.userservice.user.dto.CrudDto
import kz.innlab.userservice.user.dto.PermissionDTO
import kz.innlab.userservice.user.model.Permission
import kz.innlab.userservice.user.service.PermissionService
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

/**
 * @project mugalim-backend
 * @author bekzat on 06.02.2023
 */
@RestController
@RequestMapping("/permissions")
class PermissionController {
    @Autowired
    lateinit var service: PermissionService

    @GetMapping("/{id}")
    @PreAuthorize("#oauth2.hasScope('server') or hasAnyRole('ADMIN', 'CONTENT_MAKER')")
    fun getById(@PathVariable id: UUID, principal: Principal): Optional<Permission> {
        return service.getById(id, principal.name)
    }

    @GetMapping("/pageable")
    @PreAuthorize("#oauth2.hasScope('server') or hasAnyRole('ADMIN', 'CONTENT_MAKER')")
    fun getPageable(
        @RequestParam(value = "page") page: Int? = 1,
        @RequestParam(value = "size") size: Int? = 20,
        @RequestParam params: MutableMap<String, String> = mutableMapOf(),
        principal: Principal,
    ): Page<Permission> {
        val pageR: PageRequest =
            PageRequest.of((page ?: 1) - 1, (size ?: 20), Sort.by(Sort.Direction.DESC, "createdAt"))
        return service.getPageable(pageR, params, principal.name)
    }

    @GetMapping("")
    @PreAuthorize("#oauth2.hasScope('server') or hasAnyRole('ADMIN', 'CONTENT_MAKER')")
    fun getList(
        @RequestParam params: MutableMap<String, String> = mutableMapOf(),
        principal: Principal,
    ): List<Permission> {
        return service.getList(params, principal.name)
    }

    @PostMapping("/crud")
//    @PreAuthorize("#oauth2.hasScope('server')")
    fun checkAccess(@RequestBody item: PermissionDTO): ResponseEntity<*> {
        return ResponseEntity(service.getPermissions(item), HttpStatus.OK)
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun create(@RequestBody item: Permission, principal: Principal): ResponseEntity<*> {
        return ResponseEntity(service.create(item, principal.name), HttpStatus.OK)
    }

    @PutMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun edit(@RequestBody item: CrudDto, principal: Principal): ResponseEntity<*> {
        return ResponseEntity(service.edit(item, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable id: UUID, principal: Principal): ResponseEntity<*> {
        return ResponseEntity(service.delete(id, principal.name), HttpStatus.OK)
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun deleteList(
        @Valid @RequestBody ids: List<UUID>,
        principal: Principal
    ): ResponseEntity<*> {
        return ResponseEntity(service.delete(ids, principal.name), HttpStatus.OK)
    }
}
