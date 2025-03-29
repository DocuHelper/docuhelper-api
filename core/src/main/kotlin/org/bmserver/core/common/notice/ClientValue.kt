package org.bmserver.core.common.notice

import java.util.UUID

class ClientValue (
    val clients: MutableMap<UUID,Int> = mutableMapOf()
)
