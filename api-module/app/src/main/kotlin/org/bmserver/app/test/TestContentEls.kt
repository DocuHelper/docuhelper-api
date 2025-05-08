package org.bmserver.app.test

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.util.UUID

@Document(indexName = "test_content_v1", alwaysWriteMapping = false)
class TestContentEls(
    @Id
    val uuid: UUID,
    val content: String?,
    @Field(name = "embed_openai_3_small", type = FieldType.Dense_Vector)
    var embedOpenai3Small: List<Float>? = null,
    @Field(name = "embed_openai_3_large", type = FieldType.Dense_Vector)
    var embedOpenai3Large: List<Float>? = null
) {

}

@Document(indexName = "test_qa_v1", alwaysWriteMapping = false)
class TestQAEls(
    @Id
    var uuid: UUID,
    val contentId: UUID,
    val q: String,
    val a: String,
    @Field(name = "embed_openai_3_small", type = FieldType.Dense_Vector)
    var embedOpenai3Small: List<Float>? = null,
    @Field(name = "embed_openai_3_large", type = FieldType.Dense_Vector)
    var embedOpenai3Large: List<Float>? = null
) {

}