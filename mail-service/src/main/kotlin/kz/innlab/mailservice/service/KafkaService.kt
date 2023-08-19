package kz.innlab.mailservice.service

import kz.innlab.mailservice.model.MailMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 20.09.2022
 */
@Service
class KafkaService {

    @Autowired
    lateinit var smtpService: SmtpService

    @KafkaListener(topics = ["msg"])
    fun orderListener(record: ConsumerRecord<String, MailMessage>) {
        println(record.partition())
        println(record.key())
        val result = smtpService.sendmail(record.value())
    }

}
