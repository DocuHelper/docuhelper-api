package org.bmserver.kafka.config

import org.bmserver.core.common.domain.event.EventKey
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.SenderOptions

@Configuration
class KafkaConfig {
    @Bean
    fun reactiveKafkaProducerTemplate(props: KafkaProperties): ReactiveKafkaProducerTemplate<Any, Any> {
        return ReactiveKafkaProducerTemplate(
            SenderOptions.create(props.buildProducerProperties())
        )
    }

    @Bean
    fun reactiveKafkaConsumerTemplate(props: KafkaProperties): ReactiveKafkaConsumerTemplate<EventKey, LinkedHashMap<String, String>> {
        val consumerProperties = props.buildConsumerProperties()
        consumerProperties.putAll(
            mapOf(
                "group.id" to "docuhelper-api",
                "spring.json.trusted.packages" to "*",
                "spring.json.key.default.type" to "org.bmserver.core.common.domain.event.EventKey",
                "spring.json.value.default.type" to "java.util.LinkedHashMap"
            )
        )

        val opt = ReceiverOptions.create<EventKey, LinkedHashMap<String, String>>(consumerProperties)
            .subscription(
                listOf(
                    "docuhelper-document-parser",
                )
            )

        return ReactiveKafkaConsumerTemplate(opt)
    }
}
