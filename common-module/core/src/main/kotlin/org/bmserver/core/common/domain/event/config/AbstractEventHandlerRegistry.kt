package org.bmserver.core.common.domain.event.config

import org.bmserver.core.common.domain.event.AbstractEvent
import org.bmserver.core.common.domain.event.EventHandler
import org.springframework.beans.factory.config.BeanPostProcessor

/**
 * EventHandler 관리
 */
abstract class AbstractEventHandlerRegistry : BeanPostProcessor {
    protected val eventHandlerMap: MutableMap<Class<out AbstractEvent>, MutableList<EventHandler<out AbstractEvent>>> =
        mutableMapOf()
    protected val eventClassMap: MutableMap<String, Class<out AbstractEvent>> = mutableMapOf()

    fun getEventHandler(eventClass: Class<out AbstractEvent>): List<EventHandler<*>> {
        return eventHandlerMap[eventClass] ?: emptyList()
    }

    fun getEventType(eventSimpleName: String): Class<out AbstractEvent>? {
        return eventClassMap[eventSimpleName]
    }

}