package org.bmserver.app.common.config.security

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationConverter(
    private val jwtUtil: org.bmserver.app.common.config.security.JwtUtil,
) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        val request: ServerHttpRequest = exchange!!.request

        val jwtCookie = request.cookies["JWT_TOKEN"] ?: return Mono.empty()

        val token = jwtCookie.get(0).value

        val user: org.bmserver.app.common.config.security.User

        try {
            val userInfo = jwtUtil.parseJwt(token)
            val email = userInfo["sub"] as String ?: return Mono.empty()

            user =
                org.bmserver.app.common.config.security
                    .User(email)
        } catch (e: ExpiredJwtException) {
            println(e)
            return Mono.empty()
        }

        val authentication = UsernamePasswordAuthenticationToken(user, token, listOf())

        println("로그인 성공")
        return Mono.just(authentication)
    }
}

class User(
    var email: String,
)
