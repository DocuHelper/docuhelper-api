package org.bmserver.gateway.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class WebFluxConfig : WebFluxConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("*")
            .allowedOrigins("*")
            .allowCredentials(true)
            .maxAge(3600)

        // Add more mappings...
    }
}

@Configuration
class CorsConfig {
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*") // 모든 도메인 허용 (보안 이슈 주의)
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = false // 필요한 경우 true 설정

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}

// @Configuration
// class GraphQLCorsConfig {
//    @Bean
//    fun corsWebFilter(): CorsWebFilter {
//        val corsConfig =
//            CorsConfiguration().apply {
//                allowedOrigins = listOf("*") // 모든 도메인 허용
//                allowedMethods = listOf("GET", "POST", "OPTIONS")
//                allowedHeaders = listOf("*")
//            }
//
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/graphql", corsConfig)
//        source.registerCorsConfiguration("/subscriptions", corsConfig) // WebSocket 서브스크립션 지원
//
//        return CorsWebFilter(source)
//    }
// }
