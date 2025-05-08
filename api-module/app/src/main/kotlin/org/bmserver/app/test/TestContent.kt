package org.bmserver.app.test

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.util.UUID

class TestContent(
    val content: String,
    @Column(value = "embed_openai_3_small")
    var embedOpenai3Small: List<Float>? = null,
    @Column(value = "embed_openai_3_large")
    var embedOpenai3Large: List<Float>? = null
) {
    @Id
    lateinit var uuid: UUID

}

data class TestQA(
    val contentId: UUID,
    val q: String,
    val a: String,
){
    @Column(value = "embed_openai_3_small")
    var embedOpenai3Small: List<Float>? = null
    @Column(value = "embed_openai_3_large")
    var embedOpenai3Large: List<Float>? = null
    @Id
    lateinit var uuid: UUID

}