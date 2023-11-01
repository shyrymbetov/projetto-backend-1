package kz.innlab.authservice.auth.dto

import kz.innlab.authservice.auth.model.payload.UserProviderType
import javax.validation.constraints.NotNull

data class NewUserDto(
    val email: String = "",
    val username: String = "",
    val name: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    val emailOptional: String? = null,
    val provider: @NotNull UserProviderType? = null,
    val phone: String? = null
)
