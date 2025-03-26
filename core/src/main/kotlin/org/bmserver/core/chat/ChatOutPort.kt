package org.bmserver.core.chat

import org.bmserver.core.chat.model.Chat
import org.bmserver.core.common.domain.CommonDomainService
import reactor.core.publisher.Mono
import java.util.UUID

interface ChatOutPort : CommonDomainService<Chat> {
    fun updateAnswer(uuid:UUID, answer: String): Mono<Chat>
}