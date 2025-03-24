package org.bmserver.app.chat

import org.bmserver.core.chat.model.Chat
import org.bmserver.core.common.domain.BaseDomainQueryRepository
import org.bmserver.core.common.domain.BaseDomainRepository
import org.bmserver.core.common.domain.BaseDomainService
import org.springframework.stereotype.Component

@Component
class ChatAdapter(
    private val baseDomainRepository: BaseDomainRepository<Chat>,
    private val baseDomainQueryRepository: BaseDomainQueryRepository<Chat>
) : BaseDomainService<Chat>(baseDomainRepository, baseDomainQueryRepository)
