package org.bmserver.app.user.token.hisotry

import org.bmserver.core.common.domain.BaseDomainRepository
import org.bmserver.core.user.token.history.UserTokenHistory
import org.springframework.stereotype.Repository

@Repository
interface UserTokenHistoryRepository:BaseDomainRepository<UserTokenHistory> {
}