package kz.innlab.carservice.system.service

import org.springframework.context.annotation.Configuration
import java.util.*


@Configuration
class Util {
    companion object {

        fun generateRandomUuid(): UUID {
            return UUID.randomUUID()
        }

        fun getRandomString(length: Int) : String {
            val charset = ('a'..'z') + ('0'..'9')

            return List(length) { charset.random() }
                .joinToString("")
        }
    }
}

