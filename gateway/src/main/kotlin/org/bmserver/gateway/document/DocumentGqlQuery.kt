package org.bmserver.gateway.document

import org.bmserver.core.common.BaseDomainService
import org.bmserver.core.document.model.Document
import org.bmserver.gateway.common.AbstractDomainQueryGateway
import org.bmserver.gateway.document.request.DocumentQueryRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class DocumentGqlQuery(
    private val baseDomainService: BaseDomainService<Document>,
) : AbstractDomainQueryGateway<Document>(baseDomainService) {
    fun findDocument(query: DocumentQueryRequest): Flux<Document> = find(query.toDomainQuery())
}
