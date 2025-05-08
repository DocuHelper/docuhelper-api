package org.bmserver.gateway.chunk

import org.bmserver.core.document.chunk.ChunkOutPort
import org.bmserver.core.document.chunk.model.Chunk
import org.bmserver.gateway.common.AbstractDomainQueryGateway
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ChunkGqlQuery(
    private val chunkOutPort: ChunkOutPort
) : AbstractDomainQueryGateway<Chunk>(
    chunkOutPort
) {
    fun findChunkByEmbedValue(document: UUID, text: String) =
        chunkOutPort.findChunkByEmbed(document, text, 3).collectList()

}