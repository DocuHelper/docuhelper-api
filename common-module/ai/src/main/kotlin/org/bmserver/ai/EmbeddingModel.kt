package org.bmserver.ai

import reactor.core.publisher.Mono

interface EmbeddingModel {
    fun getEmbedding(text: String): Mono<List<Float>>
}