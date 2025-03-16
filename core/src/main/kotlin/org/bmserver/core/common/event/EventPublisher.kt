package org.bmserver.core.common.event

import reactor.core.publisher.Mono

interface EventPublisher {
    fun publish(event: AbstractEvent): Mono<Void>
}