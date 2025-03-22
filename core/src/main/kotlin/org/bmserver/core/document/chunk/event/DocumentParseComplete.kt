package org.bmserver.core.document.chunk.event

import org.bmserver.core.common.domain.event.AbstractEvent
import java.util.UUID

data class DocumentParseComplete(
    val documentUuid: UUID
) : AbstractEvent() {
}