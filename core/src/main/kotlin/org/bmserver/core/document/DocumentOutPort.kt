package org.bmserver.core.document

import org.bmserver.core.common.domain.CommonDomainService
import org.bmserver.core.document.model.Document
import org.bmserver.core.document.model.DocumentState
import reactor.core.publisher.Mono
import java.util.UUID

interface DocumentOutPort : CommonDomainService<Document> {
    fun updateDocumentState(uuid: UUID, state: DocumentState): Mono<Document>
}
