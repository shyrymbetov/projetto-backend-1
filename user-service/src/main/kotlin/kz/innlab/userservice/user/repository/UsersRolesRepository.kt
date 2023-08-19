package kz.innlab.userservice.user.repository

import kz.innlab.userservice.user.model.UsersRoles
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 13.04.2022
 */
interface UsersRolesRepository: JpaRepository<UsersRoles, UUID> {
    fun findAllByRoleIdIn(roleIds: List<UUID>): List<UsersRoles>

    @Transactional
    fun deleteAllByUserId(userId: UUID): Long
}
