package org.bmserver.app.chat

import org.bmserver.core.chat.model.Chat
import org.bmserver.core.common.domain.BaseDomainQueryRepository
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component

@Component
class ChatQueryRepository(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
) : BaseDomainQueryRepository<Chat>(r2dbcEntityTemplate, Chat::class.java)