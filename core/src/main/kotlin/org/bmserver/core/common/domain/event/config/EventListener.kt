package org.bmserver.core.common.domain.event.config

import org.bmserver.core.common.domain.event.EventKey
import reactor.core.publisher.Flux

/**
 * 메세지 브로커를 통해 이벤트를 읽어옴
 */
interface EventListener {
    fun consumeEvent(): Flux<Pair<EventKey, LinkedHashMap<String, String>>>
}