package kz.innlab.authservice.auth.dto

import java.util.UUID

class EmailChangeDTO {
    val userId: UUID? = null
    val token: String? = null
    val newEmail: String? = null
}
