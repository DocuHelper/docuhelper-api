package org.bmserver.ai.openai

import org.bmserver.ai.EmbeddingModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class OpenAiEmbeddingClient(
    private val openAiClient: WebClient
) : EmbeddingModel {
    @Value("\${spring.ai.openai.embedding.options.model}")
    lateinit var model: String
    override fun getEmbedding(text: String): Mono<List<Float>> {

        val request = OpenAiEmbeddingRequest(
            input = text,
            model = model,
        )

        return openAiClient.post()
            .uri("/v1/embeddings")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OpenAiEmbeddingResponse::class.java)
            .map { it.data.first().embedding }

    }
}