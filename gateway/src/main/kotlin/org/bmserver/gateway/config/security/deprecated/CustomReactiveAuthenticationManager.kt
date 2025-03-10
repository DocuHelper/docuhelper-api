package org.bmserver.gateway.config.security.deprecated

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono

// @Component
class CustomReactiveAuthenticationManager : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> =
        Mono.just(
            UsernamePasswordAuthenticationToken(authentication.principal, authentication.credentials, authentication.authorities),
        )
}
