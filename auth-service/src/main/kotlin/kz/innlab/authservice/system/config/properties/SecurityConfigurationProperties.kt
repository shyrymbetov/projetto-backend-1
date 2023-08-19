package kz.innlab.authservice.system.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "security.authentication")
class SecurityConfigurationProperties {
    var registration = Registration()
    var authorization = Authorization()
    var resetPassword = ResetPassword()

    inner class Registration {
        var duration: Long? = 0
        var failedCount: Int? = 0
    }

    inner class ResetPassword {
        var duration: Int? = 0
        var failedCount: Int? = 0
    }

    inner class Authorization {
        var failedCount: Int? = 0
        var blockTime: Long? = 0
        var providers = Providers()

        inner class Providers {
            var google = Google()

            inner class Google {
                var clientIds: ArrayList<String> = arrayListOf()
            }
        }
    }
}
