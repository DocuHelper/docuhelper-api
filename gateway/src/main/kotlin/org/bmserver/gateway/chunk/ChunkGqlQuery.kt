package org.bmserver.gateway.chunk

import kotlinx.coroutines.reactor.awaitSingle
import org.bmserver.core.document.chunk.ChunkOutPort
import org.bmserver.core.document.chunk.model.Chunk
import org.bmserver.core.document.chunk.model.result.ChunkWithSimilarity
import org.bmserver.gateway.common.AbstractDomainQueryGateway
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ChunkGqlQuery(
    private val chunkOutPort: ChunkOutPort
) : AbstractDomainQueryGateway<Chunk>(
    chunkOutPort
) {
    suspend fun findChunkByEmbedValue(document: UUID, embed: List<Float>): List<ChunkWithSimilarity> {
        return chunkOutPort.findChunkByEmbed(document, embed).collectList().awaitSingle()
    }
}