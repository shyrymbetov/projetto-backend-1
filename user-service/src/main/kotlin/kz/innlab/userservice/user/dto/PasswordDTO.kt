package kz.innlab.userservice.user.dto

import java.util.UUID

class PasswordDTO {

    var userId: UUID? = null
    var oldPassword: String? = null
    var token: String? = null
//    @ValidPassword
    var newPassword: String? = null
}
