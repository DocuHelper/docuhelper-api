package org.bmserver.gateway.chunk.dataloader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import kotlinx.coroutines.reactor.awaitSingle
import org.bmserver.core.document.chunk.ChunkOutPort
import org.bmserver.core.document.chunk.model.Chunk
import org.bmserver.core.document.chunk.model.ChunkQuery
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ChunkDataLoader(
    private val chunkOutPort: ChunkOutPort
) : KotlinDataLoader<UUID, Chunk> {
    override val dataLoaderName: String
        get() = "ChunkDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, Chunk> {
        return DataLoaderFactory.newDataLoader<UUID, Chunk>(
            { uuids, env ->
                CoroutineScope(Dispatchers.Default).future {
                    println(uuids)
                    chunkOutPort.find(ChunkQuery(uuids, null)).collectList().awaitSingle()
                }
            },
            DataLoaderOptions.newOptions().setCachingEnabled(false)
                .setBatchLoaderContextProvider { graphQLContext }
        )
    }
}