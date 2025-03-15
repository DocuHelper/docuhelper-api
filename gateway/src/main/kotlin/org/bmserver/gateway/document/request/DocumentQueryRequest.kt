package org.bmserver.gateway.document.request

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bmserver.core.document.model.DocumentQuery
import org.bmserver.core.document.model.DocumentState
import org.bmserver.gateway.common.AbstractAuthRequest
import java.util.UUID

data class DocumentQueryRequest(
    val uuid: UUID?,
    val name: String?,
    val state: DocumentState?,
    @GraphQLIgnore val owner: UUID?,
) : AbstractAuthRequest() {
    @GraphQLIgnore
    fun toDomainQuery(): DocumentQuery =
        DocumentQuery(
            uuid,
            name,
            state,
            owner = requestUser.uuid,
        )
}
