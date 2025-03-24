package org.bmserver.gateway.chat

import org.bmserver.core.chat.model.Chat
import org.bmserver.core.common.domain.CommonDomainService
import org.bmserver.gateway.chat.request.ChatSendRequest
import org.bmserver.gateway.common.AbstractDomainMutationGateway
import org.springframework.stereotype.Component

@Component
class ChatGqlMutation(
    private val commonDomainService: CommonDomainService<Chat>
) : AbstractDomainMutationGateway<Chat>(commonDomainService) {
    suspend fun send(request: ChatSendRequest): Chat {
        return create(
            Chat(
                document = request.document,
                userUuid = request.requestUser.uuid,
                userAsk = request.ask,
            )
        )
    }
}