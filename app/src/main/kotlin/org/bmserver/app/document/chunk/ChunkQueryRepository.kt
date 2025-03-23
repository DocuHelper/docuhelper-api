package org.bmserver.app.document.chunk

import org.bmserver.core.common.domain.BaseDomainQueryRepository
import org.bmserver.core.document.chunk.model.Chunk
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component

@Component
class ChunkQueryRepository(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
) : BaseDomainQueryRepository<Chunk>(r2dbcEntityTemplate, Chunk::class.java)