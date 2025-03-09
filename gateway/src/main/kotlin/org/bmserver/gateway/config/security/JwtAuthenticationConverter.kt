package org.bmserver.gateway.config.security

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class JwtAuthenticationConverter(
    private val jwtUtil: JwtUtil,
) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        val request: ServerHttpRequest = exchange!!.request

        val jwtCookie = request.cookies["JWT_TOKEN"] ?: return Mono.empty()

        val token = jwtCookie.get(0).value

        val user: User

        try {
            val userInfo = jwtUtil.parseJwt(token)
            val userUuid = userInfo["sub"] as String
            val userEmail = userInfo["email"] as String ?: return Mono.empty()

            user = User(UUID.fromString(userUuid), userEmail)
        } catch (e: ExpiredJwtException) {
            println(e)
            return Mono.empty()
        }

        val authentication = UsernamePasswordAuthenticationToken(user, token, listOf())

        return Mono.just(authentication)
    }
}
