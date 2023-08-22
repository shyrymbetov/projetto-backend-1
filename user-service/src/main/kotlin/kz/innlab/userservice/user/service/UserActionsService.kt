package kz.innlab.userservice.user.service

import kz.innlab.userservice.system.dto.EmailChangeDTO
import kz.innlab.userservice.user.dto.PasswordDTO
import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.dto.UserRequest
import java.security.Principal

interface UserActionsService {
    fun sendVerifyCodeToEmail(email: String): Status
    fun changePasswordByToken(passwordDto: PasswordDTO): Status
    fun changePassword(principal: Principal, passwordDTO: PasswordDTO): Status
    fun hasRole(roles: List<String>, username: String): Status
    fun checkEmail(newUserRequest: UserRequest): Status
    fun changeStatusBlocked(statusFilter: MutableMap<String, String>): Status
    fun verifyUserEmail(email: EmailChangeDTO): Status
    fun changeUserEmail(emailDto: EmailChangeDTO): Status
}
