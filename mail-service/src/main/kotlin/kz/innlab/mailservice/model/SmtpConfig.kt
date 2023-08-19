package kz.innlab.mailservice.model

import javax.validation.constraints.NotNull

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 15.06.2022
 */
data class SmtpConfig(
    @NotNull
    var host: String? = null,

    @NotNull
    var port: Int? = null,

    // There will not always be mail
    @NotNull
    var username: String? = null,

    @NotNull
    var password: String? = null,

    @NotNull
    var smtpStartTls: Boolean = true,

    @NotNull
    var smtpAuth: Boolean = true,

    @NotNull
    var protocol: String? = "smtps",

    var debug: Boolean = false,

) {
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "host" to host,
            "port" to port,
            "username" to username,
            "password" to password,
            "smtpStartTls" to smtpStartTls,
            "smtpAuth" to smtpAuth,
            "protocol" to protocol,
            "debug" to debug
        )
    }
}
