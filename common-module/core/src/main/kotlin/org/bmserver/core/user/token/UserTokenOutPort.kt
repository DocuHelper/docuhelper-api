package org.bmserver.core.user.token

import org.bmserver.core.common.domain.CommonDomainService
import org.bmserver.core.user.token.model.UserToken
import reactor.core.publisher.Mono
import java.util.UUID

interface UserTokenOutPort : CommonDomainService<UserToken> {
    fun updateUserToken(user: UUID, diff: Int): Mono<UserToken>;
}