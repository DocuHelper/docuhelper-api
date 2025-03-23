package org.bmserver.core.common.domain.event

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.lang.reflect.ParameterizedType

@Component
interface EventHandler<T : AbstractEvent> {
    fun handle(event: T): Mono<Void>

    @Suppress("UNCHECKED_CAST")
    fun getEventClass(): Class<T> {
        val type = (this::class.java.genericInterfaces.first() as ParameterizedType).actualTypeArguments.first()
        return type as Class<T>
    }
}