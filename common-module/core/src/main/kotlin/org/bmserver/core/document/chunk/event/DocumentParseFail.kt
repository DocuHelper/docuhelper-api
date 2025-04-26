package org.bmserver.core.document.chunk.event

import org.bmserver.core.common.domain.event.AbstractEvent
import java.util.UUID

class DocumentParseFail(
    val documentUuid: UUID
) : AbstractEvent()