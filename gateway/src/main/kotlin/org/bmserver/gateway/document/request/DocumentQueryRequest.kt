package org.bmserver.gateway.document.request

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bmserver.core.common.domain.Pagination
import org.bmserver.core.document.model.DocumentQuery
import org.bmserver.core.document.model.DocumentState
import org.bmserver.gateway.common.AbstractAuthRequest
import java.util.UUID

data class DocumentQueryRequest(
    val uuid: UUID?,
    val name: String?,
    val state: DocumentState?,
    val pagination: Pagination?,
    @GraphQLIgnore val owner: UUID?,
) : AbstractAuthRequest() {
    @GraphQLIgnore
    fun toDomainQuery(): DocumentQuery =
        DocumentQuery(
            uuid,
            name,
            state,
            pagination = pagination,
            owner = requestUser.uuid,
        )
}
