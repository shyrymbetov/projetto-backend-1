package kz.innlab.authservice.system.service.security

import kz.innlab.authservice.auth.model.User
import kz.innlab.authservice.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
abstract class AbstractProviderService {

    @Autowired
    private lateinit var userRepository: UserRepository

    abstract fun checkToken(tokenId: String): User

    fun getUserCreateIfNotExistsByEmail(token: kz.innlab.authservice.auth.dto.NewUserDto): User {
        var user = User()
        userRepository.findByEmailIgnoreCaseAndDeletedAtIsNull(token.email!!).ifPresentOrElse({
            user = it
        }, {
            user.email = token.email
            user.name = token.name
            user.firstName = token.firstName ?: token.email
            user.lastName = token.lastName
            user.enabled = true
            user.provider = token.provider
            user.password = UUID.randomUUID().toString()
            userRepository.save(user)
        })

        return user
    }

}
