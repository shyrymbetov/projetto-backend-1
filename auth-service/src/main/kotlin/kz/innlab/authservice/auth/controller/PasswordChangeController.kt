package kz.innlab.authservice.auth.controller

import kz.innlab.authservice.auth.dto.PasswordDTO
import kz.innlab.authservice.auth.service.SecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/security")
class PasswordChangeController {

    @Autowired
    private lateinit var securityService: SecurityService

    @PostMapping("/token/password")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun changePasswordToken(
        @Valid @RequestBody passwordDto: PasswordDTO
    ): kz.innlab.authservice.auth.dto.Status {
        return securityService.changeUserPasswordToken(passwordDto)
    }

    @PostMapping("/check-token")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun showChangePasswordPage(
        @RequestParam("token") token: String
    ): kz.innlab.authservice.auth.dto.Status {
        return securityService.validatePasswordResetToken(token)
    }

    @PostMapping("/user/change-password")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun changePassword(
        @Valid @RequestBody passwordDto: PasswordDTO,
    ): kz.innlab.authservice.auth.dto.Status {
        return securityService.changeUserPassword(passwordDto)
    }

    //There give back token
    @PostMapping("/user/reset-password/{username}")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun resetPassword(@PathVariable(value = "username") username: String): String? {
        return securityService.createPasswordResetTokenForUser(username)
    }
}
