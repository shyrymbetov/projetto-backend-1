package kz.innlab.mailservice.config

import kz.innlab.mailservice.model.MailMessage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.LongDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer


/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 19.09.2022
 */
@Configuration
class KafkaConsumerConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    private val kafkaServer: String? = null

    @Value("\${spring.kafka.consumer.group-id}")
    private val kafkaGroupId: String? = null

    @Bean
    fun consumerConfigs(): Map<String, Any?> {
        val props: MutableMap<String, Any?> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServer!!
        props[ConsumerConfig.GROUP_ID_CONFIG] = kafkaGroupId!!
        return props
    }

    @Bean
    fun kafkaListenerConsumerFactory(): DefaultKafkaConsumerFactory<String, MailMessage> {
        val errorHandlingDeserializer: ErrorHandlingDeserializer<MailMessage> = ErrorHandlingDeserializer(
            JsonDeserializer(
                MailMessage::class.java, false
            )
        )
        return DefaultKafkaConsumerFactory(
            consumerConfigs(),
            StringDeserializer(),
            errorHandlingDeserializer
        )
    }


    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, MailMessage> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, MailMessage> =
            ConcurrentKafkaListenerContainerFactory<String, MailMessage>()
        factory.consumerFactory = kafkaListenerConsumerFactory()
        return factory
    }
}
