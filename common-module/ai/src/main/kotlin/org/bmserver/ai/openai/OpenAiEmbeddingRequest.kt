package org.bmserver.ai.openai

class OpenAiEmbeddingRequest(
    val input: String,
    val model: String,
    val encoding_format: String = "float"
) {
}