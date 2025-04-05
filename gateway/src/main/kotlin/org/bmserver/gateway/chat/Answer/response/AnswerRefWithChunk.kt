package org.bmserver.gateway.chat.Answer.response

import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import graphql.schema.DataFetchingEnvironment
import org.bmserver.core.chat.model.ChatAnswerRef
import org.bmserver.core.document.chunk.model.Chunk
import java.util.UUID

class AnswerRefWithChunk(
    val answerRef: ChatAnswerRef,
) {
    fun chunk(environment: DataFetchingEnvironment) =
        environment.getValueFromDataLoader<UUID, Chunk>("ChunkDataLoader", answerRef.chunk)
}
