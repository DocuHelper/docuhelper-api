package org.bmserver.gateway.config.filter

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
                allowedOrigins = listOf(
                    "https://docuhelper.bmserver.org/",
                    "http://localhost:3000"
                )
                allowedMethods = listOf("POST", "OPTIONS") // 필요한 HTTP 메서드 추가
                allowedHeaders = listOf("*") // 모든 헤더 허용
                allowCredentials = true // 인증 정보 포함 허용 (필요 시)
            }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/graphql", corsConfig)
        source.registerCorsConfiguration("/subscriptions", corsConfig)

        return CorsWebFilter(source)
    }
}
