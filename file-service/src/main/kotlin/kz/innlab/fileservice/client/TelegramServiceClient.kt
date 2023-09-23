package kz.innlab.fileservice.client

import kz.innlab.fileservice.dto.TelegramMessageDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

@FeignClient(name = "telegram-service", path = "telegram")
interface TelegramServiceClient {

    @PostMapping("/send")
    fun sendMessage(@Valid @RequestBody message: TelegramMessageDto)
}
