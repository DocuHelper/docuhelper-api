package org.bmserver.app.chat

import org.bmserver.core.chat.ChatOutPort
import org.bmserver.core.chat.event.ChatSend
import org.bmserver.core.chat.model.Chat
import org.bmserver.core.chat.model.ChatState
import org.bmserver.core.common.domain.BaseDomainQueryRepository
import org.bmserver.core.common.domain.BaseDomainRepository
import org.bmserver.core.common.domain.BaseDomainService
import org.bmserver.core.common.domain.event.config.EventPublisher
import org.bmserver.core.common.notice.UserNotifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class ChatAdapter(
    private val baseDomainRepository: BaseDomainRepository<Chat>,
    private val baseDomainQueryRepository: BaseDomainQueryRepository<Chat>,
    private val userNotifier: UserNotifier,
    private val eventPublisher: EventPublisher
) : ChatOutPort, BaseDomainService<Chat>(baseDomainRepository, baseDomainQueryRepository) {
    override fun create(model: Chat): Mono<Chat> {
        return super.create(model).flatMap {
            eventPublisher.publish(ChatSend(it)).thenReturn(it)
        }
    }

    override fun updateAnswer(uuid: UUID, answer: String): Mono<Chat> {
        return baseDomainRepository.findById(uuid)
            .flatMap {
                it.result = answer
                it.state = ChatState.COMPETE
                baseDomainRepository.save(it)
            }
            .flatMap {
                userNotifier.send(it.userUuid, it).thenReturn(it)
            }
    }
}
