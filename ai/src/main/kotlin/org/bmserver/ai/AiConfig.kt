package org.bmserver.ai

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class AiConfig {
    @Value("\${spring.ai.ollama.base-url}")
    lateinit var ollamaBaseUrl: String

    @Bean
    fun ollamaClient(): WebClient {
        return WebClient.builder()
            .baseUrl(ollamaBaseUrl)
            .build()
    }

}