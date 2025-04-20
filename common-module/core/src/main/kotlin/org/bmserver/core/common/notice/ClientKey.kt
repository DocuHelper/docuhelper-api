package org.bmserver.core.common.notice

import java.util.UUID

class ClientKey (
    val user: UUID
) {
    override fun hashCode(): Int {
        return user.hashCode()
    }
}