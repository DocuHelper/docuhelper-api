package org.bmserver.gateway.document

import org.bmserver.core.common.CommonDomainService
import org.bmserver.core.document.model.Document
import org.bmserver.gateway.common.AbstractDomainMutationGateway
import org.bmserver.gateway.config.security.SecurityUtil
import org.bmserver.gateway.document.request.CreateDocumentRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DocumentGqlMutation(
    private val commonDomainService: CommonDomainService<Document>,
) : AbstractDomainMutationGateway<Document>(
        commonDomainService,
    ) {
    fun createDocument(request: CreateDocumentRequest): Mono<Document> =
        SecurityUtil
            .getRequestUser()
            .doOnNext { println(it) }
            .flatMap { create(request.toUseCase()) }
}
