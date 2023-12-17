package kz.innlab.userservice.user.controller

import kz.innlab.userservice.user.dto.*
import kz.innlab.userservice.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @GetMapping("/list")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun getUserListByIds(): List<UserResponse> {
        return userService.getUserList()
    }

    @PostMapping("/author")
    @PreAuthorize("isAuthenticated()")
    fun getUserAuthorsListByFullName(@Valid @RequestBody search: String? = ""): List<UserShortDto> {
        return userService.getUserAuthorsListByFullName(search)
    }

    @GetMapping("/{id}")
    @PreAuthorize("#oauth2.hasScope('server') or isAuthenticated()")
    fun getUserById(
        @PathVariable(value = "id") id: UUID,
        principal: Principal
    ): Optional<UserResponse> {
        return userService.getUserById(id)
    }

    @PostMapping("/registration")
    fun registration(@Valid @RequestBody user: RegistrationUserDto): ResponseEntity<*> {
        return ResponseEntity(userService.registration(user), HttpStatus.OK)
    }

    @PostMapping("")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    fun createNewAccount(
        @Valid @RequestBody user: UserRequest
    ): Status {
        return userService.createNewUser(user)
    }

    @PutMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun editUser(
        @Valid @RequestBody user: UserRequest
    ): Status {
        return userService.saveChanges(user)
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun removeAccount(@PathVariable(value = "id") id: UUID): Status {
        return userService.moveToTrash(id)
    }

    @DeleteMapping("/")
    fun removeAllAccount(): Status {
        return userService.deleteAllAccounts()
    }
}
