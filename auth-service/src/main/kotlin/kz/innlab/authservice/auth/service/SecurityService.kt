package kz.innlab.authservice.auth.service

import kz.innlab.authservice.auth.dto.PasswordDTO
import kz.innlab.authservice.auth.dto.Status

interface SecurityService {

    fun createPasswordResetTokenForUser(username: String): String
    fun validatePasswordResetToken(token: String): Status
    fun changeUserPassword(passwordDto: PasswordDTO): Status
    fun changeUserPasswordToken(passwordDto: PasswordDTO): Status
}
