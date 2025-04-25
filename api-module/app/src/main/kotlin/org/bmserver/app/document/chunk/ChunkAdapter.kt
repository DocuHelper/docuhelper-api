package org.bmserver.app.document.chunk

import org.bmserver.core.ai.AiOutPort
import org.bmserver.core.common.domain.BaseDomainService
import org.bmserver.core.document.chunk.ChunkOutPort
import org.bmserver.core.document.chunk.model.Chunk
import org.bmserver.core.document.chunk.model.result.ChunkWithSimilarity
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.util.UUID

@Component
class ChunkAdapter(
    private val baseDomainRepository: ChunkRepository,
    private val baseDomainQueryRepository: ChunkQueryRepository,
    private val aiOutPort: AiOutPort
) : ChunkOutPort, BaseDomainService<Chunk>(
    baseDomainRepository = baseDomainRepository,
    baseDomainQueryRepository = baseDomainQueryRepository
) {
    override fun findChunkByEmbed(document: UUID, text: String): Flux<ChunkWithSimilarity> {
        return aiOutPort.getEmbedding(text)
            .toFlux()
            .flatMap { baseDomainQueryRepository.findByEmbed(document, it) }
    }
}