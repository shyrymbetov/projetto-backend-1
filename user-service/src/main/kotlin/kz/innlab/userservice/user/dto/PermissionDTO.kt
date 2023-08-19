package kz.innlab.userservice.user.dto

import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 07.02.2023
 */
data class PermissionDTO(
    var userId: UUID?,
    var module: String?,
    var chapter: String?
)
