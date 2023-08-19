package kz.innlab.userservice.system.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.scheduling.annotation.Async
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 15.09.2022
 */
@Configuration
@Async
class FreemakerConfig {
    @get:Primary
    @get:Bean
    val freeMarkerConfiguration: FreeMarkerConfigurationFactoryBean
        get() {
            val bean = FreeMarkerConfigurationFactoryBean()
            bean.setTemplateLoaderPath("classpath:/templates/")
            return bean
        }
}
