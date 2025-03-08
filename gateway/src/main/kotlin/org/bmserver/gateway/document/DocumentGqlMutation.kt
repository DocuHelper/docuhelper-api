package org.bmserver.gateway.document

import org.bmserver.core.common.CommonDomainService
import org.bmserver.core.document.model.Document
import org.bmserver.gateway.common.AbstractDomainMutationGateway
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DocumentGqlMutation(
    private val commonDomainService: CommonDomainService<Document>,
) : AbstractDomainMutationGateway<Document>(
        commonDomainService,
    ) {
    fun createDocument(document: Document): Mono<Document> = create(document)
}
