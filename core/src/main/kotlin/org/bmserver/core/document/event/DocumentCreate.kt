package org.bmserver.core.document.event

import org.bmserver.core.common.event.AbstractEvent
import org.bmserver.core.document.model.Document

class DocumentCreate(val document: Document) : AbstractEvent()
