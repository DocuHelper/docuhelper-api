package org.bmserver.kafka.config

import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import org.bmserver.core.common.domain.event.AbstractEvent
import org.bmserver.core.common.domain.event.EventKey
import org.bmserver.core.common.domain.event.config.EventPublisher
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class KafkaEventPublisher(
    val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<Any, Any>
) : EventPublisher {
    override fun publish(event: AbstractEvent): Mono<Void> {

        val header = mutableListOf(
            RecordHeader("eventType", event::class.simpleName.toString().toByteArray())
        )

        val key = EventKey(eventType = event::class.simpleName.toString())

        val record = ProducerRecord<Any, Any>(
            KafkaTopic.DOCUHELPER_API.value,
            null,
            key,
            event,
            header
        )

        return reactiveKafkaProducerTemplate.send(record).then()
    }
}

