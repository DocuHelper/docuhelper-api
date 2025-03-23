package org.bmserver.app.document.chunk.eventHandler

import org.bmserver.core.common.domain.event.EventHandler
import org.bmserver.core.document.chunk.ChunkOutPort
import org.bmserver.core.document.chunk.event.DocumentParse
import org.bmserver.core.document.chunk.model.Chunk
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DocumentParseEventHandler(
    private val chunkOutPort: ChunkOutPort
) : EventHandler<DocumentParse> {
    override fun handle(event: DocumentParse): Mono<Void> {

        return chunkOutPort.create(
            Chunk(
                document = event.documentUuid,
                page = event.page,
                content = event.content,
                embedContent = event.embedContent,
            )
        ).then()
    }
}