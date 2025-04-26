package org.bmserver.app.document.chunk.eventHandler

import org.bmserver.core.common.domain.event.EventHandler
import org.bmserver.core.document.DocumentOutPort
import org.bmserver.core.document.chunk.event.DocumentParseFail
import org.bmserver.core.document.model.DocumentState
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DocumentParseFailEventHandler(
    private val documentOutPort: DocumentOutPort
): EventHandler<DocumentParseFail> {
    override fun handle(event: DocumentParseFail): Mono<Void> {
        return documentOutPort.updateDocumentState(event.documentUuid, DocumentState.Fail).then()
    }
}