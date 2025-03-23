package org.bmserver.app.document.chunk.eventHandler

import org.bmserver.core.common.domain.event.EventHandler
import org.bmserver.core.common.logger
import org.bmserver.core.document.chunk.event.DocumentParse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DocumentParseEventHandler : EventHandler<DocumentParse> {
    override fun handle(event: DocumentParse): Mono<Void> {
        logger.info { event } //TODO
        return Mono.empty()
    }
}