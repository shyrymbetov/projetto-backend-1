package kz.innlab.authservice.auth.dto.auth

import kz.innlab.authservice.auth.model.payload.UserProviderType

data class UserLoginRequestDto (
        val email: String? = null,
        val password: String? = null,
        var provider: UserProviderType? = null,
        val token: String? = null
)
