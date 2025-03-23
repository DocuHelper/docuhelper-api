package org.bmserver.core.common.domain.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.lang.reflect.ParameterizedType

@Component
abstract class EventHandler<T> : ApplicationRunner {

    @Autowired
    private lateinit var eventListener: EventListener

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    abstract fun handle(event: T): Mono<Void>

    override fun run(args: ApplicationArguments?) {
        eventListener.consumeEvent()
            .filter { it.first.eventType == getEventClass().simpleName }
            .map { objectMapper.convertValue(it.second, getEventClass()) }
            .flatMap { handle(it) }
            .subscribe()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getEventClass(): Class<T> {
        val type = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<T>
    }
}