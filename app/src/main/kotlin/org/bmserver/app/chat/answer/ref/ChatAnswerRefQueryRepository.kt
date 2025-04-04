package org.bmserver.app.chat.answer.ref

import org.bmserver.core.chat.model.ChatAnswerRef
import org.bmserver.core.common.domain.BaseDomainQueryRepository
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component

@Component
class ChatAnswerRefQueryRepository(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
) : BaseDomainQueryRepository<ChatAnswerRef>(r2dbcEntityTemplate, ChatAnswerRef::class.java) {
}