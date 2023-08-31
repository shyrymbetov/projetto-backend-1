package kz.innlab.userservice.system.client

import kz.innlab.userservice.system.dto.EmailChangeDTO
import kz.innlab.userservice.user.dto.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@FeignClient(name = "auth-service", path = "uaa")
interface AuthServiceClient {

    @PostMapping("/security/change-password")
    fun changePassword(@Valid @RequestBody passwordDto: PasswordDTO): Status


    //There give back token
    @PostMapping("/security/generate-code")
    fun generateCode(@RequestBody username: String): String?

    //change password after verify token
    @PostMapping("/security/reset-password")
    fun changePasswordByToken(@Valid @RequestBody passwordDto: PasswordDTO): Status

    @PostMapping("/security/email-verify")
    fun verifyEmail(@Valid @RequestBody emailDto: EmailChangeDTO): Status

    @PostMapping("/security/change-email")
    fun changeEmail(@Valid @RequestBody emailDto: EmailChangeDTO): Status
}
