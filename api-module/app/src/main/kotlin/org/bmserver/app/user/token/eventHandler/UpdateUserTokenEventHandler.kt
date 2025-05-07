package org.bmserver.app.user.token.eventHandler

import org.bmserver.core.common.domain.event.EventHandler
import org.bmserver.core.user.token.UserTokenOutPort
import org.bmserver.core.user.token.event.UpdateUserToken
import org.bmserver.core.user.token.history.UserTokenHistory
import org.bmserver.core.user.token.history.UserTokenHistoryOutPort
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateUserTokenEventHandler(
    private val userTokenHistoryOutPort: UserTokenHistoryOutPort,
    private val userTokenOutPort: UserTokenOutPort
) : EventHandler<UpdateUserToken> {
    override fun handle(event: UpdateUserToken): Mono<Void> {
        val newHistory = UserTokenHistory(
            userUuid = event.userUuid,
            type = event.type,
            diff = event.diff
        )
        return userTokenHistoryOutPort.create(newHistory)
            .flatMap { userTokenOutPort.updateUserToken(event.userUuid, event.diff) }
            .then()
    }
}