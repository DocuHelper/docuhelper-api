package org.bmserver.gateway.chat.Answer

import org.bmserver.core.chat.answer.ref.AnswerRefOutPort
import org.bmserver.core.chat.model.ChatAnswerRef
import org.bmserver.gateway.chat.Answer.request.AnswerRefQueryRequest
import org.bmserver.gateway.chat.Answer.response.AnswerRefWithChunk
import org.bmserver.gateway.common.AbstractDomainQueryGateway
import org.springframework.stereotype.Component

@Component
class AnswerRefGqlQuery(
    private val answerRefOutPort: AnswerRefOutPort
) : AbstractDomainQueryGateway<ChatAnswerRef>(answerRefOutPort) {
    suspend fun findAnswerRef(request: AnswerRefQueryRequest) = find(request.toQuery())
    suspend fun findAnswerRefWithChunk(request: AnswerRefQueryRequest) =
        find(request.toQuery()).map { AnswerRefWithChunk(it) }
}