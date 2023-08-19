package kz.innlab.authservice.auth.model.payload

import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 13.04.2022
 */
class NewUserRequest {
    var id: UUID? = null
    var email: String = ""
        set(value) {
            field = value.trim().lowercase()
        }
    var password: String = ""
    var roles: ArrayList<String> = arrayListOf()
}
