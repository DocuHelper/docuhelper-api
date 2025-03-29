package org.bmserver.gateway.chat

import com.expediagroup.graphql.server.operations.Subscription
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetchingEnvironment
import org.bmserver.core.common.Config
import org.bmserver.core.common.notice.SubScriptionNotifier
import org.bmserver.core.common.notice.UserClientManager
import org.bmserver.gateway.config.gql.getRequestUser
import org.bmserver.gateway.config.security.User
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class ChatGqlSubscription(
    private val userClientManager: UserClientManager,
    private val subscriptionNotifier: SubScriptionNotifier,
    private val objectMapper: ObjectMapper
) : Subscription {
    val serverUuid = Config.serverUuid

    fun subChat(environment: DataFetchingEnvironment): Flux<String> {
        val requestUser = environment.getRequestUser()

        return subscriptionNotifier.sub(requestUser.uuid)
            .map { objectMapper.writeValueAsString(it.second) }
            .doOnSubscribe { onConnected(requestUser) }
            .doOnCancel { disconnected(requestUser) }
            .doOnTerminate { disconnected(requestUser) }

    }

    private fun onConnected(requestUser: User) {
        userClientManager.addClient(requestUser.uuid).subscribe()
    }

    private fun disconnected(requestUser: User) {
        userClientManager.removeClient(requestUser.uuid).subscribe()
    }
}