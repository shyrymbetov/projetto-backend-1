package kz.innlab.authservice.auth.service

import kz.innlab.authservice.auth.model.Role
import kz.innlab.authservice.auth.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

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

    override fun getUserRolePriority(id: UUID): Long{
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
}
