package org.bmserver.event

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bmserver.core.common.domain.event.EventKey
import org.bmserver.core.common.domain.event.config.EventListener
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

val logger = KotlinLogging.logger { }

@Component
class KafkaEventListener(
    private val reactiveKafkaConsumerTemplate: ReactiveKafkaConsumerTemplate<EventKey, LinkedHashMap<String, String>>
) : EventListener {

    override fun consumeEvent(): Flux<Pair<EventKey, LinkedHashMap<String, String>>> = reactiveKafkaConsumerTemplate
        .receiveAutoAck()
        .doOnNext {
            logger.info { "Receive Event ${it.key()}" }
        }
        .map { Pair(it.key(), it.value()) }
}


