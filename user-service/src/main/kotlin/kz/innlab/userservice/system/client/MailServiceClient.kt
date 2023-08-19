package kz.innlab.userservice.system.client

import kz.innlab.userservice.system.dto.MailMessageDto
import kz.innlab.userservice.user.dto.Status
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "mail-service")
interface MailServiceClient {

    @PostMapping("/mail/smtp/send")
    fun sendMessage(mailMessage: MailMessageDto): Status
}
