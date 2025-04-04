package org.bmserver.app.document.chunk

import org.bmserver.core.common.domain.BaseDomainService
import org.bmserver.core.document.chunk.ChunkOutPort
import org.bmserver.core.document.chunk.model.Chunk
import org.bmserver.core.document.chunk.model.result.ChunkWithSimilarity
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.UUID

@Component
class ChunkAdapter(
    private val baseDomainRepository: ChunkRepository,
    private val baseDomainQueryRepository: ChunkQueryRepository
) : ChunkOutPort, BaseDomainService<Chunk>(
    baseDomainRepository = baseDomainRepository,
    baseDomainQueryRepository = baseDomainQueryRepository
) {
    override fun findChunkByEmbed(document: UUID, embedContent: List<Float>): Flux<ChunkWithSimilarity> {
        return baseDomainQueryRepository.findByEmbed(document,embedContent)
    }
}