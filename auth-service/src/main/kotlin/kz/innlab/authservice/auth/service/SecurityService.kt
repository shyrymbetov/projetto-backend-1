package kz.innlab.authservice.auth.service

import kz.innlab.authservice.auth.dto.EmailChangeDTO
import kz.innlab.authservice.auth.dto.PasswordDTO
import kz.innlab.authservice.auth.dto.Status

interface SecurityService {

    fun createTokenForUser(username: String): String
    fun validatePasswordResetToken(token: String): Status
    fun changeUserPassword(passwordDto: PasswordDTO): Status
    fun changeUserPasswordByToken(passwordDto: PasswordDTO): Status
    fun changeUserEmail(emailDto: EmailChangeDTO): Status
    fun verifyUserEmail(emailDto: EmailChangeDTO): Status
}
