package org.bmserver.core.common.domain.event

import java.time.LocalDateTime

data class EventKey(
    val eventType: String,
    val eventPublishDt: LocalDateTime = LocalDateTime.now()
)