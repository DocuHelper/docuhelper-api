package org.bmserver.app.test

import com.fasterxml.jackson.annotation.JsonProperty

data class BatchEmbeddingResponse(
    val id: String,
    val custom_id: String,
    val response: Response,
    val error: Any?
)

data class Response(
    val status_code: Int,
    val request_id: String,
    val body: Body
)

data class Body(
    @JsonProperty("object")
    val objectType: String,

    val data: List<EmbeddingData>,

    val model: String,

    val usage: Usage
)

data class EmbeddingData(
    @JsonProperty("object")
    val objectType: String,

    val index: Int,

    val embedding: List<Float>
)

data class Usage(
    val prompt_tokens: Int,
    val total_tokens: Int
)