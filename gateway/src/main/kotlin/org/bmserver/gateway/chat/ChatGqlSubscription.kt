package org.bmserver.gateway.chat

import com.expediagroup.graphql.server.operations.Subscription
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetchingEnvironment
import org.bmserver.core.common.Config
import org.bmserver.core.common.notice.SubScriptionNotifier
import org.bmserver.gateway.config.gql.getRequestUser
import org.bmserver.gateway.config.redis.ChatClientKey
import org.bmserver.gateway.config.redis.ChatClientValue
import org.bmserver.gateway.config.security.User
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class ChatGqlSubscription(
    private val redisOperationsChatClient: ReactiveRedisOperations<ChatClientKey, ChatClientValue>,
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
        val key = ChatClientKey(requestUser.uuid)

        redisOperationsChatClient
            .opsForHash<ChatClientKey, ChatClientValue>()
            .get(key, key)
            .defaultIfEmpty(ChatClientValue())
            .map {
                val clientCount = it.clients.get(serverUuid)?.let { it + 1 } ?: 1
                it.clients.put(serverUuid, clientCount)
                it
            }
            .flatMap {
                redisOperationsChatClient
                    .opsForHash<ChatClientKey, ChatClientValue>()
                    .put(key, key, it)
            }
            .subscribe()
    }

    private fun disconnected(requestUser: User) {
        val key = ChatClientKey(requestUser.uuid)

        redisOperationsChatClient
            .opsForHash<ChatClientKey, ChatClientValue>()
            .get(key, key)
            .map {
                val clientCount = it.clients.get(serverUuid)?.let { it - 1 } ?: 0

                if (clientCount == 0) {
                    it.clients.remove(serverUuid)
                } else {
                    it.clients.put(serverUuid, clientCount)
                }

                it
            }
            .flatMap {
                if (it.clients.isEmpty()) {
                    redisOperationsChatClient
                        .opsForHash<ChatClientKey, ChatClientValue>()
                        .remove(key, key)
                } else {
                    redisOperationsChatClient
                        .opsForHash<ChatClientKey, ChatClientValue>()
                        .put(key, key, it)
                }
            }
            .subscribe()
    }
}