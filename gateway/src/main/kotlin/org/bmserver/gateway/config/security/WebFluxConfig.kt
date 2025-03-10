package org.bmserver.gateway.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class WebFluxConfig : WebFluxConfigurer {
    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val corsConfig =
            CorsConfiguration().apply {
                allowedOrigins = listOf("*") // 모든 도메인 허용
                allowedMethods = listOf("GET", "POST", "OPTIONS")
                allowedHeaders = listOf("*")
            }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/graphql", corsConfig)
        source.registerCorsConfiguration("/subscriptions", corsConfig) // WebSocket 서브스크립션 지원

        return CorsWebFilter(source)
    }
}
