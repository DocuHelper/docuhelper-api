package org.bmserver.core.common.domain.event.config

import org.bmserver.core.common.domain.event.AbstractEvent
import reactor.core.publisher.Mono

/**
 * 메세지 브로커를 통해 이벤트 전송
 */
interface EventPublisher {
    fun publish(event: AbstractEvent): Mono<Void>
}