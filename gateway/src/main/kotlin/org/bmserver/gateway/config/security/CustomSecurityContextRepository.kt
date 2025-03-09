package org.bmserver.gateway.config.security

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class CustomSecurityContextRepository(
    private val jwtAuthenticationConverter: JwtAuthenticationConverter,
) : ServerSecurityContextRepository {
    // Stateless 설정
    override fun save(
        exchange: ServerWebExchange,
        context: SecurityContext,
    ): Mono<Void> = Mono.empty()

    // 기본 설정 사용
    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> = WebSessionServerSecurityContextRepository().load(exchange)
}
