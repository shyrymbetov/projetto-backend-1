package kz.innlab.userservice.user.controller

import kz.innlab.userservice.user.dto.UserShortDto
import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.model.User
import kz.innlab.userservice.user.dto.UserRequest
import kz.innlab.userservice.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*
import javax.validation.Valid


/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */

@RestController
@RequestMapping("")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/list")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun getUserListByIds(@Valid @RequestBody ids: List<UUID>): ArrayList<User> {
        return userService.getUserListByIds(ids)
    }

    @PostMapping("/list/archive")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun getUserListByIdsArchive(@Valid @RequestBody ids: List<UUID>): ArrayList<User> {
        return userService.getUserListByIdsArchive(ids)
    }

    @PostMapping("/list/role")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun getUserListByRole(@Valid @RequestBody roles: List<String>): ArrayList<User> {
        return userService.getUserListByRoles(roles)
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("#oauth2.hasScope('server') or isAuthenticated()")
    fun getUserById(
        @PathVariable(value = "id") id: UUID,
        principal: Principal
    ): Optional<User> {
        return userService.getUserById(id)
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun createNewAccount(
        @Valid @RequestBody user: UserRequest,
        principal: Principal
    ): Status {
        return userService.createNewUser(user)
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun removeAccount(@PathVariable(value = "id") id: UUID): Status {
        return userService.moveToTrash(id)
    }





}
