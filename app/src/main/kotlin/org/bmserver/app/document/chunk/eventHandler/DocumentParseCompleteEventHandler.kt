package org.bmserver.app.document.chunk.eventHandler

import org.bmserver.core.common.domain.event.EventHandler
import org.bmserver.core.common.logger
import org.bmserver.core.document.chunk.event.DocumentParseComplete
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DocumentParseCompleteEventHandler : EventHandler<DocumentParseComplete> {
    override fun handle(event: DocumentParseComplete): Mono<Void> {
        logger.info { event } //TODO
        return Mono.empty()
    }
}