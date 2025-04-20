package org.bmserver.app.common.config.event

import org.bmserver.core.common.domain.event.config.AbstractEventHandlerRegistry
import org.bmserver.core.common.domain.event.EventHandler
import org.springframework.stereotype.Component

@Component
class EventHandlerRegistry : AbstractEventHandlerRegistry() {
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        if (bean is EventHandler<*>) {
            val eventHandler = bean
            val eventClass = bean.getEventClass()

            val targetHandlerList = eventHandlerMap.getOrDefault(eventClass, mutableListOf())
            targetHandlerList.add(eventHandler)

            eventHandlerMap.put(eventClass, targetHandlerList)

            eventClassMap.put(eventClass.simpleName, eventClass)
        }
        return super.postProcessBeforeInitialization(bean, beanName)
    }
}