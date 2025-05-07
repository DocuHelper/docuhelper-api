package org.bmserver.gateway.document

import org.bmserver.core.document.DocumentOutPort
import org.bmserver.core.document.model.Document
import org.bmserver.gateway.common.AbstractDomainQueryGateway
import org.bmserver.gateway.document.request.DocumentQueryRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DocumentGqlQuery(
    private val documentOutPort: DocumentOutPort,
) : AbstractDomainQueryGateway<Document>(documentOutPort) {
    fun findDocument(query: DocumentQueryRequest): Mono<List<Document>> = find(query.toDomainQuery())
}
