package org.bmserver.core.ai

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AiOutPort {
    fun getEmbedding(text: String): Mono<List<Float>>

    fun getAnswer(text: String): Flux<ChatResult>
}
