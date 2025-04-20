package org.bmserver.app.chat.eventHandler

import org.bmserver.core.chat.ChatOutPort
import org.bmserver.core.chat.answer.ref.AnswerRefOutPort
import org.bmserver.core.chat.event.ChatAnswer
import org.bmserver.core.common.domain.event.EventHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ChatAnswerEventHandler(
    private val chatOutPort: ChatOutPort,
    private val answerRefOutPort: AnswerRefOutPort
) : EventHandler<ChatAnswer> {
    override fun handle(event: ChatAnswer): Mono<Void> {
        return chatOutPort.updateAnswer(event.chat, event.answer)
            .flatMap { Mono.`when`(event.answerRef.map { answerRefOutPort.create(it) }) }
            .then()
    }
}