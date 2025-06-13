package org.bmserver.app.document.chunk.dto

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.util.UUID

@Document(indexName = "chunks", alwaysWriteMapping = false)
data class ChunkEls(
    @Id
    val uuid: UUID,
    @Field(name = "document_id", type = FieldType.Keyword)
    val documentId: UUID?,
    val content: String?,
    @Field(name = "embed_content", type = FieldType.Dense_Vector)
    val embedContent: List<Float>?
) {
}