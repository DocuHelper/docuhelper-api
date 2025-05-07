package org.bmserver.core.document.event

import org.bmserver.core.common.domain.event.AbstractEvent
import java.util.UUID

class DocumentDelete(
    val uuid: UUID
) : AbstractEvent() {
}