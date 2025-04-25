package org.bmserver.app.document.chunk.eventHandler

import org.bmserver.core.common.domain.event.EventHandler
import org.bmserver.core.document.DocumentOutPort
import org.bmserver.core.document.chunk.event.DocumentParseComplete
import org.bmserver.core.document.model.DocumentState
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DocumentParseCompleteEventHandler(
    private val documentOutPort: DocumentOutPort
) : EventHandler<DocumentParseComplete> {
    override fun handle(event: DocumentParseComplete): Mono<Void> = documentOutPort
        .updateDocumentState(event.documentUuid, DocumentState.COMPLETE)
        .then()
}