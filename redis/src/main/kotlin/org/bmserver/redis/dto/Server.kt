package org.bmserver.redis.dto

import java.util.UUID

data class Server(
    val server: UUID = UUID.randomUUID()
)