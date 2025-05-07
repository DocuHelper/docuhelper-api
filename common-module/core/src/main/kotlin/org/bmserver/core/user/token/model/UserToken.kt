package org.bmserver.core.user.token.model

import org.bmserver.core.common.domain.BaseDomain
import java.util.UUID

class UserToken(
    val userUUID: UUID,
    var availableToken: Int,
    var limitToken: Int
) : BaseDomain() {
    fun updateToken(diff: Int): UserToken {
        this.availableToken += diff
        return this
    }
}