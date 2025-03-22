package org.bmserver.core.common.domain

import org.springframework.data.annotation.Id
import java.util.UUID

abstract class BaseDomain(
    @Id var uuid: UUID?,
) {
    constructor() : this(uuid = null)
}
