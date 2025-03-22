package org.bmserver.core.common.domain.event

import reactor.core.publisher.Flux

interface EventListener {
    fun consumeEvent(): Flux<Pair<EventKey, LinkedHashMap<String, String>>>
}