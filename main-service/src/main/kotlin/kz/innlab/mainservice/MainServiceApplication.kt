package kz.innlab.mainservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client


@SpringBootApplication
@EnableDiscoveryClient
@EnableOAuth2Client
@EnableFeignClients
class MainServiceApplication

fun main(args: Array<String>) {
    runApplication<MainServiceApplication>(*args)
}
