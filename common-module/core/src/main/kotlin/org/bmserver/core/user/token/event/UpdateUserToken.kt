package org.bmserver.core.user.token.event

import org.bmserver.core.common.domain.event.AbstractEvent
import org.bmserver.core.user.token.history.TokenHistoryType
import java.util.UUID

class UpdateUserToken(
    val userUuid: UUID,
    val type: TokenHistoryType,
    val diff: Int
): AbstractEvent() {
}