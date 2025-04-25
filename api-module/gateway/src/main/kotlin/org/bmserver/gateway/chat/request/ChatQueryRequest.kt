package org.bmserver.gateway.chat.request

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bmserver.core.chat.model.ChatQuery
import org.bmserver.core.common.domain.Pagination
import org.bmserver.gateway.common.AbstractAuthRequest
import java.util.UUID

class ChatQueryRequest(
    val document: UUID?,
    val pagination: Pagination?
) : AbstractAuthRequest() {

    @GraphQLIgnore
    fun toQuery(): ChatQuery {
        return ChatQuery(
            user = requestUser.uuid,
            document = document,
            pagination = pagination
        )
    }

}