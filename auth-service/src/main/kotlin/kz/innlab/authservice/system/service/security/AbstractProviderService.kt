package kz.innlab.authservice.system.service.security

import kz.innlab.authservice.auth.dto.NewUserDto
import kz.innlab.authservice.auth.model.User
import kz.innlab.authservice.auth.model.payload.UserProviderType
import kz.innlab.authservice.auth.model.payload.UserRequest
import kz.innlab.authservice.auth.repository.UserRepository
import kz.innlab.authservice.auth.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
abstract class AbstractProviderService {

    @Autowired
    private lateinit var userService: UserService

    abstract fun checkToken(tokenId: String): NewUserDto

    fun getUserCreateIfNotExists(token: NewUserDto, role: String): UUID? {
        var result: UUID? = null
        userService.getUserByEmailIgnoreCage(token.email).ifPresentOrElse({
            result = it.id
        }, {
            val user = User()
            user.email = token.email
            user.firstName = token.firstName ?: token.email
            user.lastName = token.lastName
            user.enabled = true
            user.provider = token.provider
            user.password = UUID.randomUUID().toString()
            user.roles = listOf(role)
            result = userService.create(user, listOf(role))
        })

        return result
    }

}
