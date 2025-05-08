package org.bmserver.ai

import org.bmserver.ai.openai.OpenAiEmbeddingClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class AiConfig {
    @Value("\${spring.ai.ollama.base-url}")
    lateinit var ollamaBaseUrl: String

    @Value("\${spring.ai.openai.api-key}")
    lateinit var openAiApiKey: String

    @Bean
    fun openAiEmbeddingClient(): EmbeddingModel {
        return OpenAiEmbeddingClient(openAiClient())
    }

    fun openAiClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.openai.com")
            .defaultHeader("Authorization","Bearer $openAiApiKey")
            .build()
    }

    fun ollamaClient(): WebClient {
        return WebClient.builder()
            .baseUrl(ollamaBaseUrl)
            .build()
    }
}
