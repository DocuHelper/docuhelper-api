package org.bmserver.gateway.document

import org.bmserver.core.document.DocumentOutPort
import org.bmserver.core.document.model.Document
import org.bmserver.gateway.common.AbstractDomainQueryGateway
import org.bmserver.gateway.document.request.DocumentQueryRequest
import org.springframework.stereotype.Component

@Component
class DocumentGqlQuery(
    private val documentOutPort: DocumentOutPort,
) : AbstractDomainQueryGateway<Document>(documentOutPort) {
    suspend fun findDocument(query: DocumentQueryRequest): List<Document> = find(query.toDomainQuery())
}
