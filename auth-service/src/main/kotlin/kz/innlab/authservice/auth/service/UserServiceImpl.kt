package kz.innlab.authservice.auth.service

import kz.innlab.authservice.auth.dto.Status
import kz.innlab.authservice.auth.model.User
import kz.innlab.authservice.auth.model.UsersRoles
import kz.innlab.authservice.auth.model.payload.UserRequest
import kz.innlab.authservice.auth.repository.RoleRepository
import kz.innlab.authservice.auth.repository.UserRepository
import kz.innlab.authservice.auth.repository.UsersRolesRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@Service
class UserServiceImpl: UserService {

    private val log = LoggerFactory.getLogger(javaClass)

    private val encoder = BCryptPasswordEncoder()

    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var usersRoles: UsersRolesRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    override fun getUserListByRoles(roles: List<String>): ArrayList<User> {
        var result = arrayListOf<User>()

        val rolesR = roleRepository.findAllByNameIgnoreCaseInAndDeletedAtIsNull(
            roles.map { it.trim().uppercase() }
        )
        if (rolesR.isNotEmpty()) {
            val userIds = usersRoles.findAllByRoleIdIn(rolesR.map { it.id!! }).map { it.userId!! }.toSet()
            if (userIds.isNotEmpty()) {
                result = getUserListByIds(userIds.toList())
            }
        }

        return result
    }

    override fun getUserListByIds(ids: List<UUID>): ArrayList<User> {
        return repository.findAllByIdInAndDeletedAtIsNull(ids)
    }

    override fun getUserListByIdsArchive(ids: List<UUID>): ArrayList<User> {
        return repository.findAllByIdInAndDeletedAtIsNotNull(ids)
    }

    override fun getUserById(id: UUID): Optional<User> {
        return repository.findById(id)
    }

    override fun createNewUser(user: UserRequest): Status {
        val status = Status()
        try {
            val newUser = userRequestToUser(user)
            newUser.enabled = true

            status.status = 1
            status.message = "Success"
            status.value = create(newUser)
        } catch (e: Exception) {
            log.error("Пользователь с таким адресом уже существует", e)
            status.status = 2
            status.message = "Пользователь с таким адресом уже существует"
            log.warn("Пользователь с таким адресом уже существует: " + user.email)
        }

        return status
    }

    private fun create(user: User): UUID? {
        val existing = repository.findByEmailIgnoreCaseAndDeletedAtIsNull(user.email)
        existing.ifPresent { it -> throw IllegalArgumentException("user already exists: " + it.email) }
        repository.save(user)

        return user.id
    }

    private fun userRequestToUser(userRequest: UserRequest): User {
        val newUser = User()
        newUser.firstName = userRequest.firstName
        newUser.lastName = userRequest.lastName
        newUser.email = userRequest.email
        newUser.password = userRequest.password ?:"123"
        newUser.roles = userRequest.roles
        return newUser
    }

    override fun saveChanges(user: UserRequest): Status {
        val status = Status()
        if (user.id != null) {
            repository.findById(user.id!!).ifPresent {
                it.firstName = user.firstName
                it.lastName = user.lastName
                it.avatar = user.avatar

                if (user.email.isNotBlank()) {
                    it.email = user.email
                }
                if (user.password!!.isNotBlank()) {
                    it.password = encoder.encode(user.password!!.trim())
                }
                repository.save(it)

                if (user.roles.isNotEmpty()) {
                    usersRoles.deleteAllByUserId(it.id!!)
                    setRoles(it.id!!, user.roles)
                }
                status.status = 1
                status.value = it.id
            }
        }
        return status
    }

    private fun setRoles(userId: UUID, roles: List<String>) {
        for (role in roles) {
            val roleCandidate = roleRepository.findByNameIgnoreCaseAndDeletedAtIsNull(role.uppercase())
            if (roleCandidate.isPresent) {
                val newRole = UsersRoles()
                newRole.userId = userId
                newRole.roleId = roleCandidate.get().id
                usersRoles.save(newRole)
            }
        }
    }

    //    status=0 if email and phone exists
    //    status=1 if email
    override fun checkEmail(newUserRequest: UserRequest): Status {
        val status = Status()
        var emailCandidate = Optional.empty<User>()
        if (newUserRequest.email != "") emailCandidate = repository.findByEmailIgnoreCaseAndDeletedAtIsNull(newUserRequest.email)
        var isCurrentUserEmail = false
        emailCandidate.ifPresent {
            if (it.id!! == newUserRequest.id) {
                isCurrentUserEmail = true
            }
        }

        if (emailCandidate.isPresent && !isCurrentUserEmail) {
            status.status = 1
            status.message = "Email exists"
            return status
        }
        status.status = 0
        status.message = "Email does not exists"
        return status
    }

    override fun changeStatusBlocked(statusFilter: MutableMap<String, String>): Status {
        val status = Status()
        repository.findById(UUID.fromString(statusFilter["id"])).ifPresent {
            it.blocked = if (statusFilter["block"].toBoolean()) {
                status.message = "Successfully blocked!"
                Timestamp(32503680000000L)
            } else {
                status.message = "Successfully unlocked!"
                null
            }
            status.status = 1
            repository.save(it)
        }
        return status
    }

    override fun moveToTrash(id: UUID): Status {
        val status = Status()
        val user = repository.findById(id)
        if (user.isPresent) {
            user.get().deletedAt = Timestamp(System.currentTimeMillis())
            user.get().email = "${user.get().email}_deleted_${user.get().deletedAt}"
            repository.save(user.get())

            status.status = 1
        }
        return status
    }

    override fun restore(id: UUID): Status {
        val status = Status()
        val user = repository.findById(id)
        if (user.isPresent) {
            user.get().deletedAt = null
            repository.save(user.get())

            status.status = 1
        }
        return status
    }

    override fun delete(id: UUID): Status {
        val status = Status()

        return status
    }
}
