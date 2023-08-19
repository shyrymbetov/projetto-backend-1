package kz.innlab.userservice.system.service

import org.springframework.context.annotation.Configuration
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.regex.Pattern

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 03.01.2023
 */
@Configuration
class Util {
    companion object {
        private val UUID_REGEX: Pattern =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")


        fun randomString(length: Int) : String {
            val charset = ('A'..'Z') + ('a'..'z') + ('0'..'9')

            return List(length) { charset.random() }
                .joinToString("")
        }

        fun startDate(timestamp: Timestamp) {
            timestamp.hours = 0
            timestamp.minutes = 0
            timestamp.seconds = 0
        }

        fun endDate(timestamp: Timestamp) {
            timestamp.hours = 23
            timestamp.minutes = 59
            timestamp.seconds = 59
        }

        fun setTimeZone(value: Timestamp): Timestamp {
            val zdt = ZonedDateTime.of(value.toLocalDateTime(), ZoneId.of("Asia/Almaty"))
            return Timestamp.valueOf(zdt.toLocalDateTime())
        }

        @JvmStatic
        fun isUUID(string: String): Boolean {
            return UUID_REGEX.matcher(string).matches()
        }
    }
}
