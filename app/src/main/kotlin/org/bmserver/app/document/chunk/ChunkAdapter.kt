package org.bmserver.app.document.chunk

import org.bmserver.core.common.domain.BaseDomainService
import org.bmserver.core.document.chunk.model.Chunk
import org.springframework.stereotype.Component

@Component
class ChunkAdapter(
    private val baseDomainRepository: ChunkRepository,
    private val baseDomainQueryRepository: ChunkQueryRepository
) : BaseDomainService<Chunk>(
    baseDomainRepository = baseDomainRepository,
    baseDomainQueryRepository = baseDomainQueryRepository
) {
}