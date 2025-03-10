package org.bmserver.gateway.config.filter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFluxSecurity
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

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors { it.disable() }
//            .securityContextRepository(customSecurityContextRepository)
            .authorizeExchange {
                it.anyExchange().permitAll()
            }.build()
}
