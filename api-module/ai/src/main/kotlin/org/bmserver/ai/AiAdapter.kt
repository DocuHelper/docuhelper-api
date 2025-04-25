package org.bmserver.ai

import org.bmserver.ai.ollama.OllamaEmbeddingRequest
import org.bmserver.ai.ollama.OllamaEmbeddingResponse
import org.bmserver.core.ai.AiOutPort
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

    override fun getAnswer(text: String): Flux<String> {
        val prompt = Prompt(text, openAiChatProperties.options)

        return openAiChatModel.internalStream(prompt, null)
            .map { it.result.output.text ?: "" }
            .bufferTimeout(100, Duration.ofMillis(500))
            .map { it.joinToString("") }// 최대 10개 또는 500ms마다 모아서 배출
    }
}
