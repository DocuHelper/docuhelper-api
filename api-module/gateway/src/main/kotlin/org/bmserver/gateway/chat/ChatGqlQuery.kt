package org.bmserver.gateway.chat

import org.bmserver.core.chat.ChatOutPort
import org.bmserver.core.chat.model.Chat
import org.bmserver.gateway.chat.request.ChatQueryRequest
import org.bmserver.gateway.common.AbstractDomainQueryGateway
import org.springframework.stereotype.Component

@Component
class ChatGqlQuery(
    private val chatOutPort: ChatOutPort
) : AbstractDomainQueryGateway<Chat>(chatOutPort) {
    fun findChat(queryRequest: ChatQueryRequest) = find(queryRequest.toQuery())

}