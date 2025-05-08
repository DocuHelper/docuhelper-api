package org.bmserver.ai.ollama

import org.bmserver.ai.EmbeddingModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class OllamaEmbeddingClient(
    private val ollamaClient: WebClient,
): EmbeddingModel {
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