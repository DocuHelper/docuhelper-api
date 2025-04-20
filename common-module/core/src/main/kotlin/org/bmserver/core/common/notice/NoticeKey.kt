package org.bmserver.core.common.notice

import java.time.LocalDateTime
import java.util.UUID

class NoticeKey(
    val user:UUID,
    val noticePublishDt: LocalDateTime = LocalDateTime.now()
) {
}