package kz.innlab.authservice.auth.controller

import kz.innlab.authservice.auth.dto.EmailChangeDTO
import kz.innlab.authservice.auth.dto.PasswordDTO
import kz.innlab.authservice.auth.dto.Status
import kz.innlab.authservice.auth.model.EmailVerifyToken
import kz.innlab.authservice.auth.service.SecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/security")
class SecurityController {

    @Autowired
    private lateinit var securityService: SecurityService
    @PostMapping("/change-password")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun changePassword(
        @Valid @RequestBody passwordDto: PasswordDTO,
    ): Status {
        return securityService.changeUserPassword(passwordDto)
    }


    //There give back token
    @PostMapping("/generate-code")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun generateCode(@RequestBody username: String): String? {
        return securityService.createTokenForUser(username)
    }

    //change password after verify token
    @PostMapping("/reset-password")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun changePasswordToken(
        @Valid @RequestBody passwordDto: PasswordDTO
    ): Status {
        return securityService.changeUserPasswordByToken(passwordDto)
    }

    @PostMapping("/email-verify")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun verifyEmail(
        @Valid @RequestBody emailDto: EmailChangeDTO
    ): Status {
        return securityService.verifyUserEmail(emailDto)
    }

    @PostMapping("/change-email")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun changeEmail(
        @Valid @RequestBody emailDto: EmailChangeDTO
    ): Status {
        return securityService.changeUserEmail(emailDto)
    }
}
