package kz.innlab.authservice.auth.service

import kz.innlab.authservice.auth.dto.EmailChangeDTO
import kz.innlab.authservice.auth.dto.PasswordDTO
import kz.innlab.authservice.auth.dto.Status
import kz.innlab.authservice.auth.model.EmailVerifyToken
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

    @Autowired
    lateinit var emailTokenRepository: PasswordResetTokenRepository

    override fun createTokenForUser(username: String): String {
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

    override fun validatePasswordResetToken(token: String): Status {
        val status = Status()
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
            status.value = split.first().toString()
            null
        } else {
            "not matches"
        }
        return status
    }

    override fun verifyUserEmail(emailDto: EmailChangeDTO): Status {
        val status = Status()
        val result = validatePasswordResetToken(emailDto.token?:"")
        if (result.status != 1) {
            return result
        }
        passwordTokenRepository.findByToken(result.value.toString()).ifPresent { token ->
            try {
                token.user!!.enabled = true
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

    override fun changeUserPasswordByToken(passwordDto: PasswordDTO): Status {
        val status = Status()
        val result = validatePasswordResetToken(passwordDto.token?:"")
        if (result.status != 1) {
            return result
        }
        passwordTokenRepository.findByToken(result.value.toString()).ifPresent { token ->
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

    override fun changeUserEmail(emailDto: EmailChangeDTO): Status {
        val status = Status()
        if (emailDto.newEmail.isNullOrBlank()) {
            return status
        }
        val result = validatePasswordResetToken(emailDto.token?:"")
        if (result.status != 1) {
            return result
        }
        passwordTokenRepository.findByToken(result.value.toString()).ifPresent { token ->
            try {
                token.oldEmail = token.user!!.email
                token.changed = true

                token.user!!.email = emailDto.newEmail ?: ""
                repository.save(token.user!!)

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

    override fun changeUserPassword(passwordDto: PasswordDTO): Status {
        val status = Status()

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
