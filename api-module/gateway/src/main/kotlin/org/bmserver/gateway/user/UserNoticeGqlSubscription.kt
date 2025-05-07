package org.bmserver.gateway.user

import com.expediagroup.graphql.server.operations.Subscription
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetchingEnvironment
import org.bmserver.core.common.notice.SubScriptionNotifier
import org.bmserver.core.common.notice.UserClientManager
import org.bmserver.core.user.model.User
import org.bmserver.gateway.config.gql.getRequestUser
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class UserNoticeGqlSubscription(
    private val userClientManager: UserClientManager,
    private val subscriptionNotifier: SubScriptionNotifier,
    private val objectMapper: ObjectMapper
) : Subscription {
    fun subNotice(environment: DataFetchingEnvironment): Flux<String> {
        val requestUser = environment.getRequestUser()

        return subscriptionNotifier.sub(requestUser.uuid)
            .map { objectMapper.writeValueAsString(it.second) }
            .doOnSubscribe { onConnected(requestUser) }
            .doOnCancel { disconnected(requestUser) }
            .doOnTerminate { disconnected(requestUser) }
    }

    private fun onConnected(requestUser: User) {
        println("onConnected")
        userClientManager.addClient(requestUser.uuid).subscribe()
    }

    private fun disconnected(requestUser: User) {
        println("disconnected")
        userClientManager.removeClient(requestUser.uuid).subscribe()
    }
}