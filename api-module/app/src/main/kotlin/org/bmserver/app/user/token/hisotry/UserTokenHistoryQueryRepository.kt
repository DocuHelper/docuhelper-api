package org.bmserver.app.user.token.hisotry

import org.bmserver.core.common.domain.BaseDomainQueryRepository
import org.bmserver.core.user.token.history.UserTokenHistory
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component

@Component
class UserTokenHistoryQueryRepository(
    private val template: R2dbcEntityTemplate,
    ):BaseDomainQueryRepository<UserTokenHistory>(template, UserTokenHistory::class.java) {
}