package org.bmserver.gateway.config.security

import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono
import java.util.UUID

class SecurityUtil {
    companion object {
        fun getRequestUser(): Mono<User> =
            ReactiveSecurityContextHolder
                .getContext()
                .doOnNext {
                    println("SecurityUtil : ")
                }.map { securityContext ->
                    securityContext.authentication.principal as User
                }.switchIfEmpty(
                    Mono.just(
                        User(
                            uuid = UUID.randomUUID(),
                            email = "empty",
                        ),
                    ),
                )
    }
}
