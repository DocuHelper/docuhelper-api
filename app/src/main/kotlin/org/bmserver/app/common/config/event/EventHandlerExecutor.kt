package org.bmserver.app.common.config.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.bmserver.core.common.domain.event.config.AbstractEventHandlerExecutor
import org.bmserver.core.common.domain.event.config.EventListener
import org.springframework.stereotype.Component

@Component
class EventHandlerExecutor(
    private val eventHandlerRegistry: EventHandlerRegistry,
    private val eventListener: EventListener,
    private val objectMapper: ObjectMapper
) : AbstractEventHandlerExecutor(eventHandlerRegistry, eventListener, objectMapper)