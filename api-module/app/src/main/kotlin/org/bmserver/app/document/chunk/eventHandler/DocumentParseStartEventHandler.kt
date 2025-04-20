package org.bmserver.app.document.chunk.eventHandler

import org.bmserver.core.common.domain.event.EventHandler
import org.bmserver.core.document.DocumentOutPort
import org.bmserver.core.document.chunk.event.DocumentParseStart
import org.bmserver.core.document.model.DocumentState
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DocumentParseStartEventHandler(
    private val documentOutPort: DocumentOutPort
) : EventHandler<DocumentParseStart> {
    override fun handle(event: DocumentParseStart): Mono<Void> = documentOutPort
        .updateDocumentState(event.documentUuid, DocumentState.PARSING)
        .then()

}