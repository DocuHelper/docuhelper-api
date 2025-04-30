package org.bmserver.core.user.model

import java.util.UUID

data class User(
    val uuid: UUID,
    val email: String,
    val role: UserRole
)
