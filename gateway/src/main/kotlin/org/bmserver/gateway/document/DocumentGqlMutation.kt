package org.bmserver.gateway.document

import graphql.schema.DataFetchingEnvironment
import org.bmserver.core.document.DocumentOutPort
import org.bmserver.core.document.model.Document
import org.bmserver.gateway.common.AbstractDomainMutationGateway
import org.bmserver.gateway.document.request.CreateDocumentRequest
import org.springframework.stereotype.Component

@Component
class DocumentGqlMutation(
    private val documentOutPort: DocumentOutPort,
) : AbstractDomainMutationGateway<Document>(
        documentOutPort,
    ) {
    suspend fun createDocument(
        request: CreateDocumentRequest,
        environment: DataFetchingEnvironment,
    ): Document = create(request.toUseCase())
}
