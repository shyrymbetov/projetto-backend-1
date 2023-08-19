package kz.innlab.authservice.auth.controller

import kz.innlab.authservice.auth.model.Role
import kz.innlab.authservice.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*
import kotlin.collections.ArrayList

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */

@RestController
@RequestMapping("/roles")
class RoleController {

    @Autowired
    private lateinit var roleService: RoleService

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getList(principal: Principal): ArrayList<kz.innlab.authservice.auth.model.Role> {
        return roleService.getList(UUID.fromString(principal.name))
    }

    @GetMapping("/list/{userName}")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun getListByUserName(@PathVariable userName: String): ArrayList<kz.innlab.authservice.auth.model.Role> {
        return roleService.getList(UUID.fromString(userName))
    }

    @GetMapping("/list/all")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun getList(): ArrayList<kz.innlab.authservice.auth.model.Role> {
        return roleService.getList()
    }

//    @GetMapping("/priority/{userName}")
//    @PreAuthorize("#oauth2.hasScope('server')")
//    fun getUserRolePriority(@PathVariable userName: String): Long {
//        return roleService.getUserRolePriority(userName)
//    }

//    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
//    @PostMapping("/create")
//    fun createUser(@Valid @RequestBody user: NewUserRequest): UUID? {
//        return userService.create(user)
//    }
}
