package kz.innlab.authservice.auth.service

import kz.innlab.authservice.auth.model.PasswordResetToken
import kz.innlab.authservice.auth.repository.PasswordResetTokenRepository
import kz.innlab.authservice.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class SecurityServiceImpl: SecurityService {

    private val encoder = BCryptPasswordEncoder()

    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var passwordTokenRepository: PasswordResetTokenRepository

    override fun createPasswordResetTokenForUser(username: String): String {
        var token = UUID.randomUUID().toString()

        repository.findByEmailIgnoreCaseAndDeletedAtIsNull(username).ifPresentOrElse({
            val myToken = PasswordResetToken()
            myToken.token = token
            myToken.user = it

            val random = Random()
            val randomCode = String.format("%04d", random.nextInt(10000))
            token = "$token:$randomCode"
            myToken.code = encoder.encode(token)

            val cal = Calendar.getInstance()
            cal.add(Calendar.MINUTE, PasswordResetToken.EXPIRATION)
            myToken.expiryDate = cal.time

            passwordTokenRepository.save(myToken)
        }, {
            token = ""
        })

        return token
    }

    override fun validatePasswordResetToken(token: String): kz.innlab.authservice.auth.dto.Status {
        val status = kz.innlab.authservice.auth.dto.Status()
        val split = token.split(":")
        if (split.size != 2) {
            return status
        }
        val passToken = passwordTokenRepository.findByToken(split.first())
        status.message = if (!passToken.isPresent) {
            "invalidToken"
        } else if (isTokenExpired(passToken.get())) {
            "expired"
        }  else if (passToken.get().changed) {
            "used"
        } else if (encoder.matches(token, passToken.get().code!!)) {
            status.status = 1
            status.value = true
            null
        } else {
            "not matches"
        }
        return status
    }

    override fun changeUserPasswordToken(passwordDto: kz.innlab.authservice.auth.dto.PasswordDTO): kz.innlab.authservice.auth.dto.Status {
        val status = kz.innlab.authservice.auth.dto.Status()
        val split = passwordDto.token!!.split(":")
        if (split.size != 2) {
            return status
        }
        val result = validatePasswordResetToken(passwordDto.token!!)

        if (result.status != 1) {
            println("Not validated")
            return result
        }
        println("Searching TOKEN => ${split.first()}")
        passwordTokenRepository.findByToken(split.first()).ifPresent { token ->
            try {
                token.user!!.password = encoder.encode(passwordDto.newPassword)
                repository.save(token.user!!)

                token.changed = true
                passwordTokenRepository.save(token)

                status.status = 1
                status.message = "Successful"
            } catch (_: Exception) {
            }

        }
        return status
    }

    private fun isTokenFound(passToken: PasswordResetToken?): Boolean {
        return passToken != null
    }

    private fun isTokenExpired(passToken: PasswordResetToken): Boolean {
        val cal = Calendar.getInstance()
        return passToken.expiryDate!!.before(cal.time)
    }

    override fun changeUserPassword(passwordDto: kz.innlab.authservice.auth.dto.PasswordDTO): kz.innlab.authservice.auth.dto.Status {
        val status = kz.innlab.authservice.auth.dto.Status()

        repository.findByIdAndDeletedAtIsNull(passwordDto.userId!!).ifPresent {
            try {
                if (!encoder.matches(passwordDto.oldPassword, it.password)) {
                    status.message = "Old password not correct"
                    return@ifPresent
                }

                it.password = encoder.encode(passwordDto.newPassword)
                repository.save(it)

                status.status = 1
                status.message = "Successful"
            } catch (_: Exception) {
            }
        }
        return status
    }

}
