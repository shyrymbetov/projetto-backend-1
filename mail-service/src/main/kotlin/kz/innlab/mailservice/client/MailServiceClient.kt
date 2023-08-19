package kz.innlab.mailservice.client

import kz.innlab.mailservice.model.MailMessage
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "mailservice")
interface MailServiceClient {

    @GetMapping("/api/test/test")
    fun data(): String?

    @PostMapping("/api/receive/messages")
    fun getMailMessages(mailConfig: MutableMap<String, String>): Array<MailMessage>?
}
