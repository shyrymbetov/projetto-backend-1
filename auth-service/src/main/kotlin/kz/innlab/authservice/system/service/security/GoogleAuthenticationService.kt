package kz.innlab.authservice.system.service.security

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.json.webtoken.JsonWebSignature
import kz.innlab.authservice.auth.dto.NewUserDto
import kz.innlab.authservice.auth.model.User
import kz.innlab.authservice.auth.model.payload.UserProviderType
import kz.innlab.authservice.system.config.custom.GoogleIdTokenPayload
import kz.innlab.authservice.system.config.properties.SecurityConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.io.IOException
import java.security.GeneralSecurityException

@Service
@EnableConfigurationProperties(SecurityConfigurationProperties::class)
class GoogleAuthenticationService: AbstractProviderService() {

    @Autowired
    private lateinit var securityConfigurationProperties: SecurityConfigurationProperties

    private val transport = NetHttpTransport()
    private val factory = GsonFactory()

    override fun checkToken(tokenId: String): NewUserDto {
        val tokenInfo = verify(tokenId) ?: throw IllegalAccessException("Invalid id_token")
        if (!tokenInfo.payload.emailVerified || tokenInfo.payload.email.isNullOrBlank())
            throw IllegalAccessException("Invalid id_token")
        return NewUserDto(
            email = tokenInfo.payload.email,
            name = tokenInfo.payload["name"].toString(),
            firstName = tokenInfo.payload["givenName"].toString(),
            lastName = tokenInfo.payload["familyName"].toString(),
            provider = UserProviderType.GOOGLE
        )
    }

    @Throws(GeneralSecurityException::class, IOException::class)
    fun verify(idTokenString: String): GoogleIdToken? {
        val verifier = GoogleIdTokenVerifier.Builder(transport, factory)
            .setAudience(securityConfigurationProperties.authorization.providers.google.clientIds) // CLient Id
            .build()

        val jws = JsonWebSignature.parser(verifier.jsonFactory).setPayloadClass(GoogleIdTokenPayload::class.java).parse(idTokenString)

        val idToken = GoogleIdToken(
            jws.header,
            jws.payload as GoogleIdTokenPayload,
            jws.signatureBytes,
            jws.signedContentBytes
        )
        verifier.verify(idTokenString)
        return if (verifier.verify(idToken)) idToken else null
    }
}
