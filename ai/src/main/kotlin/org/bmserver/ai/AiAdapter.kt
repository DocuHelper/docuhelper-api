package org.bmserver.ai

import org.bmserver.ai.ollama.OllamaEmbeddingRequest
import org.bmserver.ai.ollama.OllamaEmbeddingResponse
import org.bmserver.core.ai.AiOutPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class AiAdapter(
    private val ollamaClient: WebClient
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
}
