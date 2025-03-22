package org.bmserver.kafka.config

import org.bmserver.core.common.domain.event.AbstractEvent
import org.bmserver.core.common.domain.event.EventKey
import org.bmserver.core.common.domain.event.EventPublisher
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class KafkaEventPublisher(
    val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<Any, Any>
) : EventPublisher {
    override fun publish(event: AbstractEvent): Mono<Void> {

        return reactiveKafkaProducerTemplate.send(
            KafkaTopic.DOCUHELPER_API.value,
            EventKey(eventType = event::class.simpleName.toString()),
            event
        ).then()
    }
}

