package org.bmserver.core.document.chunk

import org.bmserver.core.common.domain.CommonDomainService
import org.bmserver.core.document.chunk.model.Chunk
import reactor.core.publisher.Flux
import java.util.UUID

interface ChunkOutPort : CommonDomainService<Chunk> {
    fun findChunkByEmbed(document: UUID, embedContent: List<Float>): Flux<Chunk>
}