package kz.innlab.authservice.system.service.security

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import kz.innlab.authservice.system.config.properties.SecurityConfigurationProperties
import kz.innlab.authservice.model.User
import kz.innlab.authservice.model.payload.UserProviderType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service

@Service
@EnableConfigurationProperties(SecurityConfigurationProperties::class)
class GoogleAuthenticationService: AbstractProviderService() {

    @Autowired
    private lateinit var securityConfigurationProperties: SecurityConfigurationProperties

    private val transport = NetHttpTransport()
    private val factory = GsonFactory()

    override fun checkToken(tokenId: String): User {
        val verifier = GoogleIdTokenVerifier.Builder(transport, factory)
            .setAudience(securityConfigurationProperties.authorization.providers.google.clientIds) // CLient Id
            .build()

        val tokenInfo =  verifier.verify(tokenId) ?: throw IllegalAccessException("Invalid id_token")
        if (!tokenInfo.payload.emailVerified || tokenInfo.payload.email.isNullOrBlank())
            throw IllegalAccessException("Invalid id_token")
        return getUserCreateIfNotExistsByEmail(
            kz.innlab.authservice.auth.dto.NewUserDto(
                email = tokenInfo.payload.email,
                name = tokenInfo.payload["name"].toString(),
                firstName = tokenInfo.payload["given_name"].toString(),
                lastName = tokenInfo.payload["family_name"].toString(),
                provider = UserProviderType.GOOGLE
            )
        )
    }

}
