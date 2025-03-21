package org.bmserver.core.common.event

import java.time.LocalDateTime

class EventKey(
    val eventType: String,
    val eventPublishDt: LocalDateTime = LocalDateTime.now()
)