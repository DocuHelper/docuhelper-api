package org.bmserver.gateway.config.security

import java.util.UUID

data class User(
    val uuid: UUID,
    val email: String,
)
