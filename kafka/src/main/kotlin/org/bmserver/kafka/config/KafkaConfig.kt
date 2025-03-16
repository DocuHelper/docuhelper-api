package org.bmserver.kafka.config

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions

@Configuration
class KafkaConfig {
    @Bean
    fun reactiveKafkaProducerTemplate(props: KafkaProperties): ReactiveKafkaProducerTemplate<Any, Any> {
        return ReactiveKafkaProducerTemplate(
            SenderOptions.create(props.buildProducerProperties())
        )
    }

//    @Bean
//    fun reactiveKafkaConsumerTemplate(props: KafkaProperties): ReactiveKafkaConsumerTemplate{
//        return ReactiveKafkaConsumerTemplate(
//            ReceiverOptions.create()
//        )
//    }
}
