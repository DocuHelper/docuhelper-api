package org.bmserver.core.common.domain.event

import reactor.core.publisher.Mono

interface EventPublisher {
    fun publish(event: AbstractEvent): Mono<Void>
}