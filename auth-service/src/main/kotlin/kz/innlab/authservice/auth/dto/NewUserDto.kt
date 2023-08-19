package kz.innlab.authservice.auth.dto

import kz.innlab.authservice.auth.model.payload.UserProviderType
import javax.validation.constraints.NotNull

data class NewUserDto(
    val email: String? = null,
    val username: String = "",
    val name: String? = null,
    val firstName: String? = null,
    val lastName: String = "",
    val emailOptional: String? = null,
    val provider: @NotNull UserProviderType? = null
)
