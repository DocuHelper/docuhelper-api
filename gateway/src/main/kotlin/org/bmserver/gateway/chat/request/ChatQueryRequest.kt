package org.bmserver.gateway.chat.request

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bmserver.core.chat.model.ChatQuery
import org.bmserver.gateway.common.AbstractAuthRequest
import java.util.UUID

class ChatQueryRequest(
    val document: UUID?,
) : AbstractAuthRequest() {

    @GraphQLIgnore
    fun toQuery(): ChatQuery {
        return ChatQuery(
            user = requestUser.uuid,
            document = document
        )
    }

}