package org.bmserver.core.document.chunk.event

import org.bmserver.core.common.domain.event.AbstractEvent
import java.util.UUID

data class DocumentParse(
    val documentUuid: UUID,
    val content: String,
    private val embedContent: List<Float>
) : AbstractEvent()