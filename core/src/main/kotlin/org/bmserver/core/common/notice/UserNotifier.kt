package org.bmserver.core.common.notice

import reactor.core.publisher.Mono
import java.util.UUID

interface UserNotifier {
    fun send(data: AbstractNotice): Mono<Void>
    fun send(user: UUID, data: Any): Mono<Void>
}