package kz.innlab.mainservice.system.config

import feign.RequestInterceptor
import io.micrometer.core.instrument.util.IOUtils
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.ClassPathResource
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import java.io.IOException
import java.lang.RuntimeException

/**
 * @project book-service
 * @author Bakytov Nurzhan 11.07.2022
 */
@Configuration
@EnableResourceServer
class ResourceServerConfig: ResourceServerConfigurerAdapter() {

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        super.configure(resources)
    }

    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter())
    }

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        val resource = ClassPathResource("public.txt")
        var publicKey: String? = null
        try {
            publicKey = IOUtils.toString(resource.inputStream)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        converter.setVerifierKey(publicKey)
        return converter
    }

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    fun clientCredentialsResourceDetails(): ClientCredentialsResourceDetails {
        return ClientCredentialsResourceDetails()
    }

    @Bean
    fun clientCredentialsRestTemplate(): OAuth2RestTemplate {
        return OAuth2RestTemplate(clientCredentialsResourceDetails())
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices {
        val defaultTokenServices = DefaultTokenServices()
        defaultTokenServices.setTokenStore(tokenStore())
        return defaultTokenServices
    }

    @Bean
    fun oauth2FeignRequestInterceptor(): RequestInterceptor {
        return OAuth2FeignRequestInterceptor(DefaultOAuth2ClientContext(), clientCredentialsResourceDetails())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        // @formatter:off
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
            .authorizeRequests().anyRequest().permitAll()
        // @formatter:on
    }
}
