package org.bmserver.`user-client`.dto

import java.util.UUID

data class Server(
    val server: UUID = UUID.randomUUID()
)