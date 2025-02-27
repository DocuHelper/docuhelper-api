package org.bmserver.app.common.config.security

import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationWebFilter(
    private val customReactiveAuthenticationManager: org.bmserver.app.common.config.security.CustomReactiveAuthenticationManager,
    private val jwtAuthenticationConverter: org.bmserver.app.common.config.security.JwtAuthenticationConverter,
) : AuthenticationWebFilter(customReactiveAuthenticationManager) {
    init {
        this.setServerAuthenticationConverter(jwtAuthenticationConverter)
    }
}
