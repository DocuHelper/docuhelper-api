package org.bmserver.app.user.token

import org.bmserver.core.common.domain.BaseDomainRepository
import org.bmserver.core.user.token.model.UserToken
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface UserTokenRepository : BaseDomainRepository<UserToken> {
    fun findByUserUUID(user: UUID): Mono<UserToken>
}