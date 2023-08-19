package kz.innlab.mailservice.model

import java.sql.Timestamp
import javax.validation.constraints.NotNull

data class MailMessage(
    var id: String? = null,

    var from: String? = null,

    @NotNull
    var to: String? = null,

    var replyTo: String? = null,

    var cc: String? = null,
    var bcc: String? = null,

    @NotNull
    var subject: String? = null,

    @NotNull
    var content: String? = null,

    var plain: String? = null,
    var attachments: Array<MutableMap<String, String>> = arrayOf(),
    var model: MutableMap<String, String> = mutableMapOf(),
    var date: Timestamp = Timestamp(System.currentTimeMillis()),

    var smtpConfig: SmtpConfig? = null
) {

}
