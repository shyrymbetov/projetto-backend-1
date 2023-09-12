package kz.innlab.userservice.user.service

import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.model.Role
import java.util.*
import kotlin.collections.ArrayList

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 20.04.2022
 */
interface RoleService {
    fun getList(): ArrayList<Role>
    fun getList(id: UUID): ArrayList<Role>
    fun getUserRolePriority(id: UUID): Long
    fun createRoles(): Status

}
