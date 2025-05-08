package org.bmserver.app.test

data class BatchData(
    val custom_id: String,
    val body: Body_,
    val method: String = "POST",
    val url: String = "/v1/embeddings",
) {
    constructor(uuid: String, input: String) : this(uuid, Body_(input = input))
}

data class Body_(
    val input: String,
    val model: String = Model.TEXT_EMBEDDING_3_LARGE.value,
    val encoding_format: String = "float"
)

enum class Model(val value: String) {
    TEXT_EMBEDDING_3_SMALL("text-embedding-3-small"),
    TEXT_EMBEDDING_3_LARGE("text-embedding-3-large")
}