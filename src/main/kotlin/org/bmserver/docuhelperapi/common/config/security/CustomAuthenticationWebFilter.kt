package org.bmserver.docuhelperapi.common.config.security

import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationWebFilter(
    private val customReactiveAuthenticationManager: CustomReactiveAuthenticationManager,
    private val jwtAuthenticationConverter: JwtAuthenticationConverter,
) : AuthenticationWebFilter(customReactiveAuthenticationManager) {
    init {
        this.setServerAuthenticationConverter(jwtAuthenticationConverter)
    }
}
