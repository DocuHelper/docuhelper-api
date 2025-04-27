package org.bmserver.app.user.token

import org.bmserver.core.common.domain.BaseDomainQueryRepository
import org.bmserver.core.user.token.model.UserToken
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component

@Component
class UserTokenQueryRepository(
    private val template: R2dbcEntityTemplate,
) : BaseDomainQueryRepository<UserToken>(template, UserToken::class.java) {
}