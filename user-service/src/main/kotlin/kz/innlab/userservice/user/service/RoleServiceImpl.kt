package kz.innlab.userservice.user.service

import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.model.Role
import kz.innlab.userservice.user.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.security.Principal
import java.util.*
import kotlin.collections.ArrayList

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 20.04.2022
 */
@Service
class RoleServiceImpl: RoleService {

    @Autowired
    lateinit var repository: RoleRepository

    @Autowired
    lateinit var userService: UserService
    override fun getList(): ArrayList<Role> {
        return repository.findAllByDeletedAtIsNull(Sort.by(Sort.Direction.ASC, "priorityNumber"))
    }

    override fun getList(id: UUID): ArrayList<Role> {
        var roles = repository.findAllByDeletedAtIsNull(Sort.by(Sort.Direction.ASC, "priorityNumber"))
        val userRolePriority = getUserRolePriority(id)
        roles = roles.filter { it.priorityNumber!! > userRolePriority } as ArrayList<Role>
        return roles
    }

    override fun getUserRolePriority(id: UUID): Long {
        var userRolePriority = Long.MAX_VALUE
        val user = userService.getUserById(id).get()
        val roleNameToPriority = repository.findAllByDeletedAtIsNull(Sort.by(Sort.Direction.DESC, "priorityNumber"))
            .map { it.name to it.priorityNumber!! }.toMap()

        if (user.roles.isNotEmpty()) {
            if (user.roles.contains("ADMIN")) return -1

            for (role in user.roles) {
                if (roleNameToPriority.containsKey(role)) {
                    userRolePriority = Math.min(userRolePriority, roleNameToPriority[role]!!)
                }
            }
        }
        return userRolePriority
    }

    override fun createRoles(): Status {
        val roles = arrayListOf("ADMIN", "EMPLOYEE", "CLIENT")
        roles.forEach { role ->
            repository.findByNameIgnoreCaseAndDeletedAtIsNull(role).ifPresentOrElse(
                {
                    println("$role exists")
                }, {
                    val newRole = Role()
                    newRole.name = role
                    newRole.title = role
                    repository.save(newRole)
                }
            )
        }
        return Status(1, "Success")
    }
}
