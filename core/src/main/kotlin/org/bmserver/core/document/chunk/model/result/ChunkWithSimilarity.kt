package org.bmserver.core.document.chunk.model.result

import org.bmserver.core.document.chunk.model.Chunk

class ChunkWithSimilarity(
    val chunk: Chunk,
    val similarity: Float
) {
}