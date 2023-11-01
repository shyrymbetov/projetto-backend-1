package kz.innlab.userservice.user.dto

import kz.innlab.userservice.user.model.UserProviderType
import java.sql.Timestamp
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 13.04.2022
 */
class UserRequest {
    var id: UUID? = null
    var firstName: String? = null
    var lastName: String? = null
    var fio: String? = null
    var email: String = ""
    var provider: UserProviderType = UserProviderType.LOCAL
    var password: String? = ""
        set(value) {
            field = value ?: ""
        }
    var phone: String? = null
    var blocked: Timestamp? = null
    var avatar: UUID? = null
    var roles: List<String> = arrayListOf()
}
