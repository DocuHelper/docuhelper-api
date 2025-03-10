package org.bmserver.gateway.config.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

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

        if (request.method != HttpMethod.OPTIONS) {
            logger.info { "Request: ${request.getMethod()} ${request.getURI()}" }
            logger.info { request.cookies }
        }

        return chain
            .filter(exchange)
            .doOnSuccess { aVoid: Void? ->
                val duration = System.currentTimeMillis() - startTime
                if (request.method != HttpMethod.OPTIONS) {
                    logger.info { "Response: ${request.getMethod()} ${request.getURI()} - $duration ms" }
                }
            }.doOnError { error: Throwable ->
                logger.error { "Error processing request: ${error.message}" }
            }
    }
}
