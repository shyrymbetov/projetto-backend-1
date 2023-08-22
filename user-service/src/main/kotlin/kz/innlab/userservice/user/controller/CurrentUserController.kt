package kz.innlab.userservice.user.controller

import kz.innlab.userservice.user.dto.RegistrationUserDto
import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.model.User
import kz.innlab.userservice.user.dto.UserRequest
import kz.innlab.userservice.user.dto.UserResponse
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
 * @author Bekzat Sailaubayev on 29.12.2022
 */
@RestController
@RequestMapping("/current")
class CurrentUserController {

    @Autowired
    lateinit var service: UserService

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    fun getCurrentAccount(principal: Principal): Optional<UserResponse> {
        return service.getCurrentUser(principal.name)
    }

    @PutMapping("")
    @PreAuthorize("isAuthenticated()")
    fun saveCurrentAccount(@Valid @RequestBody user: UserRequest, principal: Principal): Status {
        return service.saveChangesCurrentUser(user, principal.name)
    }

    @DeleteMapping("")
    @PreAuthorize("isAuthenticated()")
    fun deleteMyAccount(principal: Principal): Status {
        return service.deleteCurrentUser(principal.name)
    }
}
