package kz.innlab.userservice.user.dto

import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 03.01.2023
 */
data class RegistrationUserDto(
    var firstName: String? = null,
    var lastName: String? = null,
    var type: RegistrationType? = RegistrationType.CLIENT,
    var email: String = "",
    var password: String? = null,
    var roles: ArrayList<String> = arrayListOf()
)
