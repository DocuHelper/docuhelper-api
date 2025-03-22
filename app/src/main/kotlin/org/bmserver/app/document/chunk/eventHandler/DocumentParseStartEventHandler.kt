package org.bmserver.app.document.chunk.eventHandler

import org.bmserver.core.common.domain.event.EventHandler
import org.bmserver.core.common.logger
import org.bmserver.core.document.chunk.event.DocumentParseStart
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DocumentParseStartEventHandler : EventHandler<DocumentParseStart>() {
    override fun handle(event: DocumentParseStart): Mono<Void> {
        logger.info { event } //TODO
        return Mono.empty()
    }
}