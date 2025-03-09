package org.bmserver.gateway.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val customSecurityContextRepository: CustomSecurityContextRepository,
) {
    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors { it.disable() }
            .securityContextRepository(customSecurityContextRepository)
            .authorizeExchange {
                it.anyExchange().permitAll()
            }.build()
}
