package org.bmserver.core.common.domain

import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.util.UUID

abstract class BaseDomain(
    var createDt: LocalDateTime = LocalDateTime.now()
) {
    @Id lateinit var uuid: UUID
}
