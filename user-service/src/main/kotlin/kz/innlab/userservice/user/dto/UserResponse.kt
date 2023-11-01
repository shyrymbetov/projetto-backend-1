package kz.innlab.userservice.user.dto

import kz.innlab.userservice.user.model.Role
import kz.innlab.userservice.user.model.UserProviderType
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

class UserResponse(
    var id: UUID? = null,
    var firstName: String? = null,
    var name: String? = null,
    var lastName: String? = null,
    var avatar: UUID? = null,
    var phone: String? = null,
    var provider: UserProviderType? = UserProviderType.LOCAL,
    var fio: String? = null,
    var email: String = "",
    var roles: List<String>,
)
