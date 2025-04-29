package org.bmserver.core.ai

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AiOutPort {
    fun getEmbedding(text: String): Mono<List<Float>>

    fun getAnswer(text: String): Flux<ChatResult<String>>

    fun <T> getAnswer(text: String, result: Class<T>): Mono<ChatResult<T>>

    fun <T> getAnswer(role:String, text: String, result: Class<T>): Mono<ChatResult<T>>
}
