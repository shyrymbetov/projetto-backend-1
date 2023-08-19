package kz.innlab.authservice.auth.repository

import kz.innlab.authservice.auth.model.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PasswordResetTokenRepository: JpaRepository<PasswordResetToken, UUID> {
    fun findByToken(token: String): Optional<PasswordResetToken>
}
