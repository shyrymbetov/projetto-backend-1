package kz.innlab.mailservice.service

import kz.innlab.mailservice.dto.Status
import kz.innlab.mailservice.config.MailConfig
import kz.innlab.mailservice.model.MailMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.mail.*
import javax.mail.internet.*


@Service
class SmtpService {

    @Value("\${spring.mail.default-encoding}")
    private val mailDefaultEncoding: String? = null

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var mailConfig: MailConfig

    @Throws(AddressException::class, MessagingException::class, IOException::class)
    fun sendmail(mail: MailMessage): Status {
        val status = Status()

        val mailSender = if (mail.smtpConfig != null) {
            setMailConfig(mail.smtpConfig!!.toMap())
        } else {
            mailConfig.mailSender
        }

        if (mail.from.isNullOrBlank()) {
            mail.from = mailSender.username
        }

        val session: Session = Session.getInstance(mailSender.javaMailProperties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(mailSender.username, mailSender.password)
            }
        })

        val msg: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(
            msg, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name()
        )

        helper.setFrom(InternetAddress(mail.from, false))
        helper.setTo(InternetAddress.parse(mail.to))
        if (mail.replyTo != null) {
            helper.setReplyTo(InternetAddress(mail.replyTo))
        }
        helper.setSubject(mail.subject!!)
        helper.setText(mail.content!!, true)
        helper.setSentDate(Date())

        val messageBodyPart = MimeBodyPart()
        messageBodyPart.setContent(mail.content, "text/html")

//            val multipart: Multipart = MimeMultipart()
//            multipart.addBodyPart(messageBodyPart)
//            val attachPart = MimeBodyPart()
//            attachPart.attachFile("/Users/bekzat/Downloads/IMG_7396.JPG")
//            multipart.addBodyPart(attachPart)
//            msg.setContent(multipart)

        mailSender.send(msg)

        status.status = 1
        status.message = "Email sent successfully"
        return status
    }

    private fun setMailConfig(config: MutableMap<String, Any?>): JavaMailSenderImpl  {
        val mailSender = JavaMailSenderImpl()
        if (config.containsKey("defaultEncoding") && config["defaultEncoding"] != null) {
            mailSender.defaultEncoding = config["defaultEncoding"].toString()
        } else {
            mailSender.defaultEncoding = mailDefaultEncoding
        }
        mailSender.host = config["host"].toString()
        mailSender.port = config["port"] as Int
        mailSender.username = config["username"].toString()
        mailSender.password = config["password"].toString()

        val javaMailProperties = Properties()
        javaMailProperties["mail.smtp.starttls.enable"] = config["smtpStartTls"].toString()
        javaMailProperties["mail.smtp.auth"] = config["smtpAuth"].toString()
        javaMailProperties["mail.transport.protocol"] = config["protocol"].toString()
        javaMailProperties["mail.debug"] = config["debug"].toString()
        mailSender.javaMailProperties = javaMailProperties
        return mailSender
    }

}
