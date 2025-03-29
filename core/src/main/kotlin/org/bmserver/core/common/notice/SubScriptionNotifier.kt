package org.bmserver.core.common.notice

import reactor.core.publisher.Flux
import java.util.UUID

interface SubScriptionNotifier {
    fun sub(user:UUID):Flux<Pair<String, LinkedHashMap<String, String>>>
}