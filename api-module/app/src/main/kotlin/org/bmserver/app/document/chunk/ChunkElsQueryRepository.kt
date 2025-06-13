package org.bmserver.app.document.chunk

import org.bmserver.app.document.chunk.dto.ChunkEls
import org.bmserver.core.common.ai.AiOutPort
import org.bmserver.core.document.chunk.model.result.ChunkWithSimilarity
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.util.UUID

@Repository
class ChunkElsQueryRepository(
    private val template: ReactiveElasticsearchOperations,
    private val chunkRepository: ChunkRepository,
    private val aiOutPort: AiOutPort
) {

    fun findSimilarChunkIds(text: String, targetDocument: UUID?, searchCount: Int): Flux<ChunkWithSimilarity> =
        aiOutPort.getEmbedding(text)
            .toFlux()
            .map { queryVector ->
                NativeQuery.builder()
                    .withQuery {
                        it.bool { b ->
                            b.must { m ->
                                targetDocument?.let {
                                    m.term {
                                        it.field("document_id")
                                            .value(targetDocument.toString())
                                    }
                                }
                            }

                            b.should { s ->
                                s.match { m ->
                                    m.field("content")
                                        .query(text)
                                        .boost(1.0f)
                                }
                                s.knn { k ->
                                    k
                                        .field("embed_content")
                                        .queryVector(queryVector)
                                        .k(5)
                                        .numCandidates(20)
                                        .boost(2.0f)
                                }
                            }
                        }
                    }
                    .build()
                    .also {
                        it.setMaxResults(searchCount)
                        it.addSourceFilter(
                            FetchSourceFilter.of(
                                arrayOf("uuid"),
                                arrayOf()
                            )
                        )
                    }
            }.flatMap {
                template.search(it, ChunkEls::class.java)
            }.flatMap { elsResult ->
                chunkRepository.findById(elsResult.content.uuid)
                    .map { Pair(elsResult, it) }
            }.map {
                ChunkWithSimilarity(
                    similarity = it.first.score,
                    chunk = it.second
                )
            }

}