package kz.innlab.authservice.system.service

import java.util.regex.Pattern

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 13.09.2022
 */
class UtilService {
    companion object {
        private val UUID_REGEX_PATTERN: Pattern =
            Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$")

        fun isValidUUID(str: String): Boolean {
            return UUID_REGEX_PATTERN.matcher(str).matches()
        }
    }
}
