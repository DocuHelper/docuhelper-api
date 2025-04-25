package org.bmserver.app.document.chunk

import org.bmserver.core.common.domain.BaseDomainQueryRepository
import org.bmserver.core.document.chunk.model.Chunk
import org.bmserver.core.document.chunk.model.result.ChunkWithSimilarity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.UUID

@Component
class ChunkQueryRepository(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
) : BaseDomainQueryRepository<Chunk>(r2dbcEntityTemplate, Chunk::class.java) {

    fun findByEmbed(document: UUID, embed: List<Float>): Flux<ChunkWithSimilarity> {

        val vectorString = embed.joinToString(prefix = "[", postfix = "]")

        val sql =
            """
            SELECT 
                uuid, page, content, num, embed_content <=> '$vectorString' AS similarity
            FROM 
                public.chunk
            WHERE 
                document = '$document'
            ORDER BY 
                similarity ASC
            """.trimIndent()

        return r2dbcEntityTemplate.databaseClient
            .sql(sql)
            .map { row, _ ->
                ChunkWithSimilarity(
                    chunk = Chunk(
                        page = row.get("page", Int::class.java) ?: -1,
                        content = row.get("content", String::class.java) ?: "",
                        embedContent = emptyList(),
                        num = row.get("num", Int::class.java) ?: -1,
                        document = document,

                        ).also { it.uuid = row.get("uuid", UUID::class.java) },
                    similarity = row.get("similarity", Float::class.java) ?: 0f,
                )
            }
            .all()
    }
}