package org.bmserver.core.document.chunk.model

import org.bmserver.core.common.domain.BaseDomain
import java.util.UUID

class Chunk(
    val document: UUID,
    val page: Int,
    val num: Int,
    val content: String,
    val embedContent: List<Float>
) : BaseDomain()