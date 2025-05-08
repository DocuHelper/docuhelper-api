package org.bmserver.app.chat.eventHandler

import org.bmserver.app.chat.notice.ChatAnswerChunk
import org.bmserver.core.chat.ChatOutPort
import org.bmserver.core.chat.answer.ref.AnswerRefOutPort
import org.bmserver.core.chat.event.ChatSend
import org.bmserver.core.chat.model.ChatAnswerRef
import org.bmserver.core.common.ai.AiOutPort
import org.bmserver.core.common.domain.event.EventHandler
import org.bmserver.core.common.domain.event.config.EventPublisher
import org.bmserver.core.common.notice.UserNotifier
import org.bmserver.core.document.chunk.ChunkOutPort
import org.bmserver.core.document.chunk.model.ChunkQuery
import org.bmserver.core.document.chunk.model.result.ChunkWithSimilarity
import org.bmserver.core.user.token.event.UpdateUserToken
import org.bmserver.core.user.token.history.TokenHistoryType
import org.bmserver.core.user.token.history.UserTokenHistoryOutPort
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux

@Component
class ChatSendEventHandler(
    private val chunkOutPort: ChunkOutPort,
    private val chatOutPort: ChatOutPort,
    private val answerRefOutPort: AnswerRefOutPort,
    private val aiOutPort: AiOutPort,
    private val userTokenHistoryOutPort: UserTokenHistoryOutPort,
    private val userNotifier: UserNotifier,
    private val eventPublisher: EventPublisher
) : EventHandler<ChatSend> {
    override fun handle(event: ChatSend): Mono<Void> {

        val userMessage = event.chat.userAsk

        val role = """
        너는 사용자의 질문을 통해 문서의 검색범위를 추측하는 모델이야
        Chunk 기반 유사도 검색을 진행해서 상위 N개의 문서기반으로 답변을 생성할지
        문서전체의 내용을 기반으로 답변을 생성할지 결정해줘
        
        """.trimIndent()

        return aiOutPort.getAnswer(role, userMessage, UserQueryType::class.java)
            .flatMap {
                when (it.message.documentScanRange) {
                    ScanRange.ALL -> searchDocumentALL(event)
                    ScanRange.CHUNK -> searchDocumentByChunk(event)
                }
            }
    }

    private fun searchDocumentALL(event: ChatSend): Mono<Void> =
        chunkOutPort.find(ChunkQuery(documentUuids = listOf(event.chat.document)))
            .map { ChunkWithSimilarity(chunk = it, similarity = 0f) }
            .collectList()
            .flatMap { generateAnswer(event, it) }


    private fun searchDocumentByChunk(event: ChatSend): Mono<Void> =
        chunkOutPort.findChunkByEmbed(event.chat.document, event.chat.userAsk, 3)
            .collectList()
            .flatMap { generateAnswer(event, it) }


    private fun generateAnswer(event: ChatSend, chunkWithSimilarities: List<ChunkWithSimilarity>): Mono<Void> {
        return chunkWithSimilarities
            .toFlux()
            .flatMap { chunkAndSimilarity ->
            // 채팅 참고 문서(Chunk) 저장
            answerRefOutPort.create(
                ChatAnswerRef(
                    chat = event.chat.uuid,
                    chunk = chunkAndSimilarity.chunk.uuid,
                    similarity = chunkAndSimilarity.similarity,
                )
            ).thenReturn(chunkAndSimilarity)
        }
            .collectList()
            .toFlux()
            // 채팅 프롬프트 생성
            .map { chunks -> chunks.map { it.chunk.content }.joinToString("=============================== \n") }
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
            // LLM 요청
            .flatMap { text ->
                aiOutPort
                    .getAnswer(text)
                    // 실시간 답변 반영
                    .flatMap {
                        userNotifier.send(
                            user = event.chat.userUuid,
                            data = ChatAnswerChunk(
                                chat = event.chat.uuid,
                                chunk = it.message
                            )
                        ).thenReturn(it)
                    }.collectList()
            }
            // 채팅 답변 저장
            .flatMap { chatResults ->
                val answer = chatResults.map { it.message }.joinToString("")
                chatOutPort.updateAnswer(event.chat.uuid, answer).thenReturn(chatResults)
            }
            .flatMap {
                val token = it.last().token
                eventPublisher.publish(
                    UpdateUserToken(
                        userUuid = event.chat.userUuid,
                        type = TokenHistoryType.CHAT,
                        diff = (token?.totalToken ?: 0) * -1
                    )
                )
            }
            .then()
    }
}

enum class ScanRange {
    ALL, CHUNK
}

data class UserQueryType(
    val documentScanRange: ScanRange
)
