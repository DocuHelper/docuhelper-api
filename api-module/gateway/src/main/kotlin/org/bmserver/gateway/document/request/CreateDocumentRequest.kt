package org.bmserver.gateway.document.request

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bmserver.core.document.model.Document
import org.bmserver.core.document.model.DocumentType
import org.bmserver.gateway.common.AbstractAuthRequest
import java.util.UUID

class CreateDocumentRequest(
    val name: String,
    val file: UUID,
    val type: DocumentType
) : AbstractAuthRequest() {
    @GraphQLIgnore
    fun toUseCase(): Document =
        Document(
            name = name,
            file = file,
            owner = requestUser.uuid,
            type = type
        )
}
