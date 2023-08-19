package kz.innlab.authservice.auth.dto

import java.util.UUID

class PasswordDTO {

    val userId: UUID? = null
    val oldPassword: String? = null
    val token: String? = null
//    @ValidPassword
    val newPassword: String? = null
}
