package org.bmserver.event

import org.bmserver.core.common.domain.event.EventKey
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.SenderOptions

@Configuration
class Config {
    @Bean
    @Qualifier
    fun reactiveKafkaProducerTemplate(props: KafkaProperties): ReactiveKafkaProducerTemplate<Any, Any> {
        val producerProperties = props.buildProducerProperties()

        return ReactiveKafkaProducerTemplate(
            SenderOptions.create(props.buildProducerProperties())
        )
    }

    /**
     * 이벤트 수신
     */
    @Bean
    fun reactiveKafkaConsumerTemplate(props: KafkaProperties): ReactiveKafkaConsumerTemplate<EventKey, LinkedHashMap<String, String>> {
        val consumerProperties = props.buildConsumerProperties()
        consumerProperties.putAll(
            mapOf(
                "group.id" to "docuhelper-api",
                "spring.json.use.type.headers" to false,
                "spring.json.trusted.packages" to "*",
                "spring.json.key.default.type" to "org.bmserver.core.common.domain.event.EventKey",
                "spring.json.value.default.type" to "java.util.LinkedHashMap",
                "key.deserializer" to "org.springframework.kafka.support.serializer.JsonDeserializer",
                "value.deserializer" to "org.springframework.kafka.support.serializer.JsonDeserializer",
            )
        )

        val opt = ReceiverOptions.create<EventKey, LinkedHashMap<String, String>>(consumerProperties)
            .subscription(
                listOf(
                    "docuhelper-document-parser",
                    "docuhelper-api"
                )
            )

        return ReactiveKafkaConsumerTemplate(opt)
    }

}