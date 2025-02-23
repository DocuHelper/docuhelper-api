package org.bmserver.docuhelperapi.common.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val customAuthenticationWebFilter: CustomAuthenticationWebFilter,
) {
    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .authorizeExchange {
                it.anyExchange().permitAll()
            }.addFilterAt(customAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
}
