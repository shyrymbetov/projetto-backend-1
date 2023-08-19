package kz.innlab.authservice.system.service.security

import kz.innlab.authservice.auth.model.payload.UserPrincipal
import kz.innlab.authservice.auth.repository.UserRepository
import kz.innlab.authservice.system.service.UtilService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*
import java.util.stream.Collectors

@Service
class UserDetailsServiceImpl: UserDetailsService {

    private val encoder = BCryptPasswordEncoder()

    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val userCandidate = if (UtilService.isValidUUID(username)) {
            userRepository.findByIdAndDeletedAtIsNull(UUID.fromString(username))
        } else {
            userRepository.findByEmailIgnoreCaseAndDeletedAtIsNull(username)
        }
        var password = ""

        val user = userCandidate.get()
            ?: throw UsernameNotFoundException("User '$username' not found")

        val isLocked = user.blocked != null && user.blocked!!.after(Timestamp(System.currentTimeMillis()))

        if (password.isBlank()) {
            password = user.password
        }

        val authorities: List<GrantedAuthority> = user.rolesCollection.stream().map { role -> SimpleGrantedAuthority("ROLE_${role.name}") }
            .collect(Collectors.toList<GrantedAuthority>())

        val userPrincipal = org.springframework.security.core.userdetails.User
            .withUsername(user.id.toString())
            .password(password)
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(isLocked)
            .credentialsExpired(false)
            .disabled(false)
            .build()

        return UserPrincipal(userPrincipal, user.id!!)
    }


}
