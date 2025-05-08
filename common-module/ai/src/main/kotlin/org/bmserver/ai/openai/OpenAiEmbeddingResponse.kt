package org.bmserver.ai.openai

class OpenAiEmbeddingResponse(
    val data: List<Result>
) {
    companion object {
        class Result(
            val embedding: List<Float>
        )
    }
}

