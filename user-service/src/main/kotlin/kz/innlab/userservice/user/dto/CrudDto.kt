package kz.innlab.userservice.user.dto

import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 06.02.2023
 */
data class CrudDto(
    var id: UUID? = null,
    var type: CrudEnum? = null,
    var value: Int? = null
)
