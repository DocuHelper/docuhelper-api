package org.bmserver.app.chat.eventHandler

import org.bmserver.app.chat.notice.ChatAnswerChunk
import org.bmserver.core.ai.AiOutPort
import org.bmserver.core.chat.ChatOutPort
import org.bmserver.core.chat.answer.ref.AnswerRefOutPort
import org.bmserver.core.chat.event.ChatSend
import org.bmserver.core.chat.model.ChatAnswerRef
import org.bmserver.core.common.domain.event.EventHandler
import org.bmserver.core.common.notice.UserNotifier
import org.bmserver.core.document.chunk.ChunkOutPort
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.util.UUID

@Component
class ChatSendEventHandler(
    private val chunkOutPort: ChunkOutPort,
    private val chatOutPort: ChatOutPort,
    private val answerRefOutPort: AnswerRefOutPort,
    private val aiOutPort: AiOutPort,
    private val userNotifier: UserNotifier
) : EventHandler<ChatSend> {
    override fun handle(event: ChatSend): Mono<Void> {
        return chunkOutPort.findChunkByEmbed(event.chat.document, event.chat.userAsk)
            .take(3)
            .toFlux()
            .flatMap { chunkAndSimilarity ->
                answerRefOutPort.create(
                    ChatAnswerRef(
                        chat = event.chat.uuid!!,
                        chunk = chunkAndSimilarity.chunk.uuid!!,
                        similarity = chunkAndSimilarity.similarity,
                    )
                ).thenReturn(chunkAndSimilarity)
            }
            .collectList()
            .toFlux()
            .map { chunks -> chunks.map { it.chunk.content }.joinToString("") }
            .map { refData ->
                val sb = StringBuilder()
                sb.append("너는 내가 보내준 자료를 기반으로 내가 원하는 정보를 찾아주는 비서야")
                sb.appendLine()
                sb.append("질문: ")
                sb.append(event.chat.userAsk)
                sb.appendLine()
                sb.append("자료: ")
                sb.appendLine()
                sb.append(refData)
                sb.toString()
            }
            .flatMap { text ->
                aiOutPort
                    .getAnswer(text)
                    .flatMap {
                        userNotifier.send(
                            user = event.chat.userUuid,
                            data = ChatAnswerChunk(
                                chat = event.chat.uuid ?: UUID.randomUUID(),
                                chunk = it
                            )
                        ).thenReturn(it)
                    }
                    .collectList()
                    .map { it.joinToString("") }
                    .flatMap {
                        chatOutPort.updateAnswer(event.chat.uuid!!, it)
                    }
            }
            .then()
    }
}

