package kz.innlab.userservice.user.service

import kz.innlab.userservice.user.dto.PasswordDTO
import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.dto.UserRequest
import java.security.Principal

interface UserActionsService {
    fun sendResetPasswordLink(email: String): Status
    fun changePassword(principal: Principal, passwordDTO: PasswordDTO): Status
    fun changeUserPasswordToken(passwordDto: PasswordDTO): Status
    fun validatePasswordResetToken(token: String): Status
    fun hasRole(roles: List<String>, username: String): Status
    fun checkEmail(newUserRequest: UserRequest): Status
    fun changeStatusBlocked(statusFilter: MutableMap<String, String>): Status
}
