package org.bmserver.gateway.config.security

import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationWebFilter(
    private val customReactiveAuthenticationManager: CustomReactiveAuthenticationManager,
    private val jwtAuthenticationConverter: JwtAuthenticationConverter,
) : AuthenticationWebFilter(customReactiveAuthenticationManager) {
    init {
        // Jwt 쿠키 인증방식 적용
        this.setServerAuthenticationConverter(jwtAuthenticationConverter)
    }
}
