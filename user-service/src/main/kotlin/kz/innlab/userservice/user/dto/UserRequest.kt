package kz.innlab.userservice.user.dto

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
    var password: String? = ""
        set(value) {
            field = value ?: ""
        }
    var blocked: Timestamp? = null
    var avatar: UUID? = null
    var roles: List<String> = arrayListOf()
}
