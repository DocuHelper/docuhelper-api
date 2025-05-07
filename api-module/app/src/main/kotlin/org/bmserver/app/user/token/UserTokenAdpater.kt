package org.bmserver.app.user.token

import org.bmserver.core.common.domain.BaseDomainService
import org.bmserver.core.user.token.UserTokenOutPort
import org.bmserver.core.user.token.model.UserToken
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.UUID

@Component
class UserTokenAdpater(
    private val userTokenRepository: UserTokenRepository,
    private val userTokenQueryRepository: UserTokenQueryRepository
) : UserTokenOutPort, BaseDomainService<UserToken>(
    userTokenRepository,
    userTokenQueryRepository
) {
    override fun updateUserToken(user: UUID, diff: Int): Mono<UserToken> {
        return getUserToken(user)
            .flatMap {
                it.availableToken += diff
                userTokenRepository.save(it)
            }
    }

    private fun getUserToken(user: UUID): Mono<UserToken> {
        return userTokenRepository.findByUserUUID(user)
            .switchIfEmpty { userTokenRepository.save(UserToken(user, 0, Int.MAX_VALUE)) }
    }
}
