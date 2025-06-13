package org.bmserver.app.document.chunk

import org.bmserver.app.document.chunk.dto.ChunkEls
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface ChunkElsRepository: ReactiveElasticsearchRepository<ChunkEls, UUID> {

}