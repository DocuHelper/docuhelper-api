package org.bmserver.ai

import org.bmserver.ai.ollama.OllamaEmbeddingRequest
import org.bmserver.ai.ollama.OllamaEmbeddingResponse
import org.bmserver.core.ai.AiOutPort
import org.bmserver.core.ai.ChatResult
import org.bmserver.core.ai.Token
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatProperties
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class AiAdapter(
    private val ollamaClient: WebClient,
    private val openAiChatModel: OpenAiChatModel,
    private val openAiChatProperties: OpenAiChatProperties,
) : AiOutPort {
    @Value("\${spring.ai.ollama.embedding.model}")
    lateinit var embeddingModel: String

    override fun getEmbedding(text: String): Mono<List<Float>> {
        val request = OllamaEmbeddingRequest(
            model = embeddingModel,
            prompt = text
        )

        return ollamaClient.post()
            .uri("/api/embeddings")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OllamaEmbeddingResponse::class.java)
            .map { it.embedding }
    }

    override fun getAnswer(text: String): Flux<ChatResult> {
        val prompt = Prompt(text, openAiChatProperties.options)

        return openAiChatModel.internalStream(prompt, null)
            .map {
                val message = it.results.filter { !it.output.text.isNullOrBlank() }
                    .map { chatResponse -> chatResponse.output.text }
                    .joinToString("")

                if (it.metadata.get<String>("finishReason").equals("STOP")) {
                    ChatResult(message)
                } else {
                    val usage = it.metadata.usage
                    val token = Token(usage.promptTokens, usage.completionTokens, usage.totalTokens)
                    ChatResult(message, token)
                }
            }
            .bufferTimeout(100, Duration.ofMillis(500))
            .map {
                val mergeMessage = it.map { chatResult -> chatResult.message }.joinToString("")
                val token = it.last().token
                ChatResult(mergeMessage, token)
            }
    }
}
