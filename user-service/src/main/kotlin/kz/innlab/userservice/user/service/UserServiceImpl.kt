package kz.innlab.userservice.user.service

import kz.innlab.userservice.user.dto.RegistrationUserDto
import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.model.User
import kz.innlab.userservice.user.model.UsersRoles
import kz.innlab.userservice.user.dto.UserRequest
import kz.innlab.userservice.user.dto.UserResponse
import kz.innlab.userservice.user.model.UserProviderType
import kz.innlab.userservice.user.repository.RoleRepository
import kz.innlab.userservice.user.repository.UserRepository
import kz.innlab.userservice.user.repository.UsersRolesRepository
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
class UserServiceImpl : UserService {

    private val log = LoggerFactory.getLogger(javaClass)

    private val encoder = BCryptPasswordEncoder()

    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var usersRoles: UsersRolesRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    override fun getUserListByRoles(roles: List<String>): List<UserResponse> {
        var result = listOf<UserResponse>()

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

    override fun getUserListByIds(ids: List<UUID>): List<UserResponse> {
        return repository.findAllByIdInAndDeletedAtIsNull(ids).map { mapUserToUserReponse(it) }
    }

    private fun mapUserToUserReponse(user: User): UserResponse {
        return UserResponse(
            user.id,
            user.firstName,
            user.name,
            user.lastName,
            user.avatar,
            user.provider,
            user.fio,
            user.email,
            user.roles,
        )
    }

    override fun getUserList(): List<UserResponse> {
        return repository.findAllByDeletedAtIsNull().map { mapUserToUserReponse(it) }
    }

    override fun getUserListByIdsArchive(ids: List<UUID>): List<UserResponse> {
        return repository.findAllByIdInAndDeletedAtIsNotNull(ids).map { mapUserToUserReponse(it) }
    }

    override fun getUserById(id: UUID): Optional<UserResponse> {
        return repository.findById(id).map { mapUserToUserReponse(it) }
    }

    override fun createNewUser(user: UserRequest): Status {
        val status = Status()
        try {
            val newUser = userRequestToUser(user)
            newUser.enabled = true

            status.status = 1
            status.message = "Success"
            status.value = create(newUser, user.roles)
        } catch (e: Exception) {
            log.error("Пользователь с таким адресом уже существует", e)
            status.status = 2
            status.message = "Пользователь с таким адресом уже существует"
            log.warn("Пользователь с таким адресом уже существует: " + user.email)
        }

        return status
    }

    private fun create(user: User, roles: List<String>): UUID? {
        val existing = repository.findByEmailIgnoreCaseAndDeletedAtIsNull(user.email)
        existing.ifPresent { it -> throw IllegalArgumentException("user already exists: " + it.email) }
        user.password = encoder.encode(user.password.trim())
        repository.save(user)
        setRoles(user.id!!, roles)
        return user.id
    }

    override fun getCurrentUser(name: String): Optional<UserResponse> {
        return getUserById(UUID.fromString(name))
    }

    private fun userRequestToUser(userRequest: UserRequest): User {
        val newUser = User()
        newUser.firstName = userRequest.firstName
        newUser.lastName = userRequest.lastName
        newUser.email = userRequest.email
        newUser.password = userRequest.password ?: "123"
        newUser.provider = UserProviderType.LOCAL
        newUser.roles = userRequest.roles
        return newUser
    }

    private fun userRequestToUser(userRequest: RegistrationUserDto): User {
        val newUser = User()
        newUser.firstName = userRequest.firstName
        newUser.lastName = userRequest.lastName
        newUser.email = userRequest.email
        newUser.provider = UserProviderType.LOCAL
        newUser.password = userRequest.password ?: "123"
        return newUser
    }

    override fun registration(user: RegistrationUserDto): Status {
        val status = Status()
        user.roles = arrayListOf(user.type.toString())
        try {
            val newUser = userRequestToUser(user)
            newUser.enabled = true
            newUser.firstName = user.firstName
            newUser.lastName = user.lastName
            newUser.email = user.email ?: ""
            newUser.password = user.password ?: "123"
            newUser.roles = user.roles
            newUser.enabled = false

            status.status = 1
            status.message = "Success"
            status.value = create(newUser, user.roles)
        } catch (e: Exception) {
            log.error("Пользователь с таким адресом уже существует", e)
            status.status = 2
            status.message = "Пользователь с таким адресом уже существует"
            log.warn("Пользователь с таким адресом уже существует: " + user.email)
        }
        return status
    }

    override fun getUserByIdForService(id: UUID): Optional<User> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun saveChangesCurrentUser(user: UserRequest, username: String): Status {
        var status = Status()
        getUserById(UUID.fromString(username)).ifPresentOrElse({
            user.id = it.id
            user.email = ""
            user.password = ""
            user.roles = listOf()

            log.info("account has been updated: " + user.email)

            status = saveChanges(user)
        }, {
            status.status = 0
            status.message = "can't find account with name ${user.email}"
        })
        return status
    }

    override fun deleteCurrentUser(name: String): Status {
        return delete(UUID.fromString(name))
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
        if (newUserRequest.email != "") emailCandidate =
            repository.findByEmailIgnoreCaseAndDeletedAtIsNull(newUserRequest.email)
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
