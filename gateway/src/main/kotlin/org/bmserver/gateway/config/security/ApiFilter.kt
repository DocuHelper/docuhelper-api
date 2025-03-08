package org.bmserver.gateway.config.security

import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
@Order(-1) // 모든 필터보다 먼저 실행되도록 설정
class ApiFilter : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> {
        val request: ServerHttpRequest = exchange.request
        val response: ServerHttpResponse = exchange.response

        val startTime = System.currentTimeMillis()

        if (request.method != HttpMethod.OPTIONS) println("Request: ${request.getMethod()} ${request.getURI()}")

        return chain
            .filter(exchange)
            .doOnSuccess { aVoid: Void? ->
                val duration = System.currentTimeMillis() - startTime
                if (request.method != HttpMethod.OPTIONS) println("Response: ${request.getMethod()} ${request.getURI()} - $duration ms")
            }.doOnError { error: Throwable ->
                println(
                    "Error processing request: ${error.message}",
                )
            }
    }
}
