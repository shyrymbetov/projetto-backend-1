package kz.innlab.mailservice.controller

import kz.innlab.mailservice.dto.Status
import kz.innlab.mailservice.model.MailMessage
import kz.innlab.mailservice.service.SmtpService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping("/smtp")
class SmtpController {

    @Autowired
    lateinit var smtpService: SmtpService

    @PostMapping("/send")
    fun sendEmail(@Valid @RequestBody mail: MailMessage): Status {
        return smtpService.sendmail(mail)
    }
}
