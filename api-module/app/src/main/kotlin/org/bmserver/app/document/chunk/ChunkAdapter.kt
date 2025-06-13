package org.bmserver.app.document.chunk

import org.bmserver.app.document.chunk.dto.ChunkEls
import org.bmserver.core.common.ai.AiOutPort
import org.bmserver.core.common.domain.BaseDomainService
import org.bmserver.core.document.chunk.ChunkOutPort
import org.bmserver.core.document.chunk.model.Chunk
import org.bmserver.core.document.chunk.model.result.ChunkWithSimilarity
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class ChunkAdapter(
    private val baseDomainRepository: ChunkRepository,
    private val baseDomainQueryRepository: ChunkQueryRepository,
    private val aiOutPort: AiOutPort,
    private val chunkElsRepository: ChunkElsRepository,
    private val chunkElsQueryRepository: ChunkElsQueryRepository
) : ChunkOutPort, BaseDomainService<Chunk>(
    baseDomainRepository = baseDomainRepository,
    baseDomainQueryRepository = baseDomainQueryRepository
) {
    override fun create(model: Chunk): Mono<Chunk> {
        return super.create(model)
            .flatMap {
                chunkElsRepository.save(
                    ChunkEls(
                        uuid = it.uuid,
                        content = it.content,
                        documentId = it.document,
                        embedContent = it.embedContent
                    )
                ).thenReturn(it)
            }
    }

    override fun findChunkByEmbed(document: UUID, text: String, searchCount: Int): Flux<ChunkWithSimilarity> {
        return chunkElsQueryRepository.findSimilarChunkIds(text, document, searchCount)
//        return aiOutPort.getEmbedding(text)
//            .toFlux()
//            .flatMap { baseDomainQueryRepository.findByEmbed(document, it) }
    }
}