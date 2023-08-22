package kz.innlab.userservice.user.controller

import kz.innlab.userservice.system.dto.EmailChangeDTO
import kz.innlab.userservice.user.dto.PasswordDTO
import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.dto.UserRequest
import kz.innlab.userservice.user.service.UserActionsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/actions")
class UserActionsController {

    @Autowired
    private lateinit var service: UserActionsService

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    fun changePassword(
        @Valid @RequestBody passwordDTO: PasswordDTO,
        principal: Principal
    ): Status {
        return service.changePassword(principal, passwordDTO)
    }


    @PostMapping("/generate-code")
    fun sendResetLink(@Valid @RequestBody map: MutableMap<String, String>): Status {
        return service.sendVerifyCodeToEmail(map["email"]?:"")
    }

    @PostMapping("/reset-password")
    fun changePasswordToken(
        @Valid @RequestBody passwordDto: PasswordDTO
    ): Status {
        return service.changePasswordByToken(passwordDto)
    }

    @PostMapping("/email-verify")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun verifyEmail(
        @Valid @RequestBody emailDto: EmailChangeDTO
    ): Status {
        return service.verifyUserEmail(emailDto)
    }

    @PostMapping("/change-email")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun changeEmail(
        @Valid @RequestBody emailDto: EmailChangeDTO
    ): Status {
        return service.changeUserEmail(emailDto)
    }

    @PostMapping("/has-role")
    @PreAuthorize("isAuthenticated()")
    fun hasRole(@Valid @RequestBody roles: List<String>, authentication: Authentication): Status {
        return service.hasRole(roles, authentication.name)
    }

    @PostMapping("/has-email")
    @PreAuthorize("isAuthenticated()")
    fun checkEmailAndPhone(@Valid @RequestBody newUserRequest: UserRequest): ResponseEntity<*> {
        return ResponseEntity(service.checkEmail(newUserRequest), HttpStatus.OK)
    }

    @PostMapping("/block")
    @PreAuthorize("hasRole('ADMIN')")
    fun changeStatusBlocked(
        @Valid @RequestBody statusFilter: MutableMap<String, String>,
    ): Status {
        return service.changeStatusBlocked(statusFilter)
    }
}
