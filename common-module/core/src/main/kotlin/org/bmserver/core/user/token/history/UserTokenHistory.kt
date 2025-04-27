package org.bmserver.core.user.token.history

import org.bmserver.core.common.domain.BaseDomain
import java.util.UUID

class UserTokenHistory(
    val userUuid: UUID,
    val type: TokenHistoryType,
    val diff: Int
) : BaseDomain()