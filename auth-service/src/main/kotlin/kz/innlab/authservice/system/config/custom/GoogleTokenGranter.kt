package kz.innlab.authservice.system.config.custom

import kz.innlab.authservice.service.security.AbstractProviderService
import org.springframework.security.authentication.AccountStatusException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException
import org.springframework.security.oauth2.provider.*
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices

class GoogleTokenGranter : AbstractTokenGranter {
    private val GRANT_TYPE = "google"
    private var providerService: AbstractProviderService

    constructor(
        providerService: AbstractProviderService,
        tokenServices: AuthorizationServerTokenServices?,
        clientDetailsService: ClientDetailsService?,
        requestFactory: OAuth2RequestFactory?
    ): super(tokenServices, clientDetailsService, requestFactory, "google") {
        this.providerService = providerService
    }

    protected constructor(
        providerService: AbstractProviderService,
        tokenServices: AuthorizationServerTokenServices?,
        clientDetailsService: ClientDetailsService?,
        requestFactory: OAuth2RequestFactory?,
        grantType: String?
    ): super(tokenServices, clientDetailsService, requestFactory, grantType) {
        this.providerService = providerService
    }

    override fun getOAuth2Authentication(client: ClientDetails, tokenRequest: TokenRequest): OAuth2Authentication? {
        val parameters: MutableMap<String?, String?> = LinkedHashMap(tokenRequest.requestParameters)
        val googleToken = parameters["token"] ?: ""
        return try {
            val user = this.providerService.checkToken(googleToken)
            val userAuth: Authentication = UsernamePasswordAuthenticationToken(user.email, null)
            val storedOAuth2Request = requestFactory.createOAuth2Request(client, tokenRequest)
            OAuth2Authentication(storedOAuth2Request, userAuth)
        } catch (var8: AccountStatusException) {
            throw InvalidGrantException("Could not authenticate user: $googleToken")
        }
    }
}
