package org.bmserver.core.common.domain.event.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.bmserver.core.common.domain.event.AbstractEvent
import org.bmserver.core.common.domain.event.EventHandler
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import reactor.core.publisher.Mono

/**
 * 메세지 브로커를 통해 수신한 이벤트를 매칭되는 이벤트 핸들러를 통해 실행시킴
 */
abstract class AbstractEventHandlerExecutor(
    private val eventHandlerRegistry: AbstractEventHandlerRegistry,
    private val eventListener: EventListener,
    private val objectMapper: ObjectMapper
) : ApplicationRunner {

    @Suppress("UNCHECKED_CAST")
    override fun run(args: ApplicationArguments?) {
        eventListener.consumeEvent()
            .flatMap {
                val eventTypeName = it.first.eventType
                val eventType = eventHandlerRegistry.getEventType(eventTypeName)
                val eventValue = eventType?.let { currentEventType -> objectMapper.convertValue(it.second, currentEventType) }

                val eventHandlers = eventType?.let { currentEventType -> eventHandlerRegistry.getEventHandler(currentEventType) } ?: emptyList()

                val resultMonos = eventHandlers.map { handler ->
                    val eventHandler = handler as EventHandler<AbstractEvent>
                    eventHandler.handle(eventValue as AbstractEvent)
                }

                Mono.`when`(resultMonos)
            }
            .subscribe()
    }

}