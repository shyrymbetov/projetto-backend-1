package kz.innlab.userservice.system.client

import kz.innlab.userservice.user.dto.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@FeignClient(name = "auth-service")
interface AuthServiceClient {

    @PostMapping("/uaa/security/user/reset-password/{username}")
    fun resetPassword(@PathVariable(value = "username") username: String): String?

    @PostMapping("/uaa/security/user/change-password")
    fun changePassword(@Valid @RequestBody passwordDTO: PasswordDTO): Status

    @PostMapping("/uaa/security/token/password")
    fun changePasswordToken(@Valid @RequestBody passwordDto: PasswordDTO): Status

    @PostMapping("/uaa/security/check-token")
    fun showChangePasswordPage(@RequestParam("token") token: String): Status

    @PostMapping("/uaa/status/last-active")
    fun getActiveUsers(@Valid @RequestBody ids: List<UUID>): List<UUID>
}
