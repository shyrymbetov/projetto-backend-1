package kz.innlab.bookservice.system.config

import kz.innlab.bookservice.system.service.AuditorAwareImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

/**
 * @project book-service
 * @author Bakytov Nurzhan 11.07.2022
 */
@Configuration
@EnableJpaAuditing
class JpaConfig {
    @Bean
    fun auditorAware(): AuditorAware<String?> {
        return AuditorAwareImpl()
    }
}
