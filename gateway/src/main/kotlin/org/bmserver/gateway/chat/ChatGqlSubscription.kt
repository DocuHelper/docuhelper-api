package org.bmserver.gateway.chat

import com.expediagroup.graphql.server.operations.Subscription
import graphql.schema.DataFetchingEnvironment
import org.bmserver.core.chat.event.ChatAnswer
import org.bmserver.gateway.config.gql.getRequestUser
import org.bmserver.gateway.config.redis.ChatClientKey
import org.bmserver.gateway.config.redis.ChatClientValue
import org.bmserver.gateway.config.security.User
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.UUID

@Component
class ChatGqlSubscription(
    private val redisOperationsChatClient: ReactiveRedisOperations<ChatClientKey, ChatClientValue>
) : Subscription {
    val serverUuid = UUID.randomUUID()

    fun subChat(environment: DataFetchingEnvironment): Flux<ChatAnswer> {
        val requestUser = environment.getRequestUser()
        val flux = Flux.interval(Duration.ofSeconds(1))
            .map {
                ChatAnswer(
                    document = UUID.randomUUID(),
                    ask = "",
                    chat = UUID.randomUUID(),
                    answer = ""
                )
            }
            .doOnSubscribe { onConnected(requestUser) }
            .doOnCancel { disconnected(requestUser) }
            .doOnTerminate { disconnected(requestUser) }
        return flux

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
                        .remove(key,key)
                } else {
                    redisOperationsChatClient
                        .opsForHash<ChatClientKey, ChatClientValue>()
                        .put(key, key, it)
                }
            }
            .subscribe()
    }
}