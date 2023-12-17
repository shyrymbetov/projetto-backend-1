package kz.innlab.authservice.system.config

import kz.innlab.authservice.system.service.security.GoogleAuthenticationService
import kz.innlab.authservice.system.config.custom.GoogleTokenGranter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.CompositeTokenGranter
import org.springframework.security.oauth2.provider.OAuth2RequestFactory
import org.springframework.security.oauth2.provider.TokenGranter
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider
import java.util.*


/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 28.03.2022
 */
@Configuration
@EnableAuthorizationServer
class OAuth2AuthorizationConfig : AuthorizationServerConfigurerAdapter() {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var clientDetailsService: ClientDetailsService

    @Autowired
    private lateinit var googleProviderService: GoogleAuthenticationService

    @Autowired
    private lateinit var env: Environment

    fun tokenGranter(): TokenGranter {
        val tokenEnhancerChain = TokenEnhancerChain()
        tokenEnhancerChain.setTokenEnhancers(listOf(CustomTokenEnhancer(), tokenEnhancer()))
        val clientDetails: ClientDetailsService = clientDetailsService
        val tokenServices = DefaultTokenServices()
        tokenServices.setTokenStore(tokenStore())
        tokenServices.setTokenEnhancer(tokenEnhancerChain)
        val requestFactory: OAuth2RequestFactory = DefaultOAuth2RequestFactory(clientDetailsService)

        val tokenGranters: MutableList<TokenGranter> = ArrayList()

        tokenGranters.add(RefreshTokenGranter(tokenServices, clientDetails, requestFactory))
        tokenGranters.add(ImplicitTokenGranter(tokenServices, clientDetails, requestFactory))
        tokenGranters.add(ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory))
        tokenGranters.add(
            ResourceOwnerPasswordTokenGranter(
                authenticationManager, tokenServices,
                clientDetails, requestFactory
            )
        )
        tokenGranters.add(
            GoogleTokenGranter(
                googleProviderService, tokenServices, clientDetailsService,
                requestFactory
            )
        )
        return CompositeTokenGranter(tokenGranters)
    }

    @Bean
    fun tokenEnhancer(): JwtAccessTokenConverter {
        val keyStoreKeyFactory = KeyStoreKeyFactory(ClassPathResource("innlab-jwt.jks"), "HY4bGbuHeWV5GrJnAPHY58SEkKTsv9fzxFeumctpRXjn3NZWA7LdQvH9KscRZmPBtXMdRP".toCharArray())
        val converter = JwtAccessTokenConverter()
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("innlab-oauth-jwt"))
        return converter
    }

    @Bean
    fun tokenStore(): JwtTokenStore {
        return JwtTokenStore(tokenEnhancer())
    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        val tokenEnhancerChain = TokenEnhancerChain()
        tokenEnhancerChain.setTokenEnhancers(listOf(CustomTokenEnhancer(), tokenEnhancer()))
        endpoints
            .authenticationManager(authenticationManager)
            .tokenGranter(tokenGranter())
            .tokenStore(tokenStore())
            .tokenEnhancer(tokenEnhancerChain)
            .accessTokenConverter(tokenEnhancer())
            .userDetailsService(userDetailsService)
    }

    @Throws(Exception::class)
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
    }

    @Throws(java.lang.Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
            .withClient("browser")
            .authorizedGrantTypes("refresh_token", "password", "google")
            .scopes("ui")
            .and()
            .withClient("user-service")
            .secret(env.getProperty("USER_SERVICE_PASSWORD"))
            .authorizedGrantTypes("client_credentials", "refresh_token")
            .scopes("server")
            .and()
            .withClient("file-service")
            .secret(env.getProperty("FILE_SERVICE_PASSWORD"))
            .authorizedGrantTypes("client_credentials", "refresh_token")
            .scopes("server")
            .and()
            .withClient("mail-service")
            .secret(env.getProperty("MAIL_SERVICE_PASSWORD"))
            .authorizedGrantTypes("client_credentials", "refresh_token")
            .scopes("server")
            .and()
            .withClient("data-service")
            .secret(env.getProperty("DATA_SERVICE_PASSWORD"))
            .authorizedGrantTypes("client_credentials", "refresh_token")
            .scopes("server")
            .and()
            .withClient("book-service")
            .secret(env.getProperty("BOOK_SERVICE_PASSWORD"))
            .authorizedGrantTypes("client_credentials", "refresh_token")
            .scopes("server")
            .and()
            .withClient("car-service")
            .secret(env.getProperty("CAR_SERVICE_PASSWORD"))
            .authorizedGrantTypes("client_credentials", "refresh_token")
            .scopes("server")
            .accessTokenValiditySeconds(20000)
            .refreshTokenValiditySeconds(20000)
    }

    @EventListener
    fun authSuccessEventListener(authorizedEvent: AuthenticationSuccessEvent) {
        if (authorizedEvent.authentication.principal is User) {

        }
    }

    @EventListener
    fun authFailedEventListener(oAuth2AuthenticationFailureEvent: AbstractAuthenticationFailureEvent) {
        // write custom code here login failed audit.
        if (oAuth2AuthenticationFailureEvent.authentication.principal is String) {

        }
    }
}
