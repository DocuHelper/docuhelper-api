package org.bmserver.gateway.chat.Answer.request

import org.bmserver.core.chat.answer.ref.AnswerRefQuery
import org.bmserver.core.common.domain.Pagination
import org.bmserver.gateway.common.AbstractAuthRequest
import java.util.UUID

class AnswerRefQueryRequest(
    val chat: UUID,
    val pagination: Pagination?
) : AbstractAuthRequest() {
    fun toQuery(): AnswerRefQuery {
        return AnswerRefQuery(chat, pagination)
    }
}