package kz.innlab.userservice.system.service

import kz.innlab.userservice.system.client.MailServiceClient
import kz.innlab.userservice.system.dto.MailMessageDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 05.01.2023
 */
@Service
class MailService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var mailClient: MailServiceClient

    @Async
    fun sendMail(mailMessage: MailMessageDto) {
        try {
            mailClient.sendMessage(mailMessage)
        } catch (e: Exception) {
            log.error(e.message)
        }
    }
}
