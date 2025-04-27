package org.bmserver.app.user.token.hisotry

import org.bmserver.core.common.domain.BaseDomainService
import org.bmserver.core.user.token.history.UserTokenHistory
import org.bmserver.core.user.token.history.UserTokenHistoryOutPort
import org.springframework.stereotype.Component

@Component
class UserTokenHistoryAdpater(
    private val userTokenHistoryRepository: UserTokenHistoryRepository,
    private val userTokenHistoryQueryRepository: UserTokenHistoryQueryRepository
) : UserTokenHistoryOutPort, BaseDomainService<UserTokenHistory>(
    baseDomainRepository = userTokenHistoryRepository,
    baseDomainQueryRepository = userTokenHistoryQueryRepository
) {
}