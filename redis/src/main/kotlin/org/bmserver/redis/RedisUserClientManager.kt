package org.bmserver.redis

import org.bmserver.core.common.Config
import org.bmserver.core.common.notice.ClientKey
import org.bmserver.core.common.notice.ClientValue
import org.bmserver.core.common.notice.UserClientManager
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class RedisUserClientManager(
    private val redisOperationsClient: ReactiveRedisOperations<ClientKey, ClientValue>
) : UserClientManager {
    override fun addClient(user: UUID): Mono<Void> {
        val serverUuid = Config.serverUuid
        val key = ClientKey(user)

        return redisOperationsClient
            .opsForHash<ClientKey, ClientValue>()
            .get(key, key)
            .defaultIfEmpty(ClientValue())
            .map {
                val clientCount = it.clients.get(serverUuid)?.let { it + 1 } ?: 1
                it.clients.put(serverUuid, clientCount)
                it
            }
            .flatMap {
                redisOperationsClient
                    .opsForHash<ClientKey, ClientValue>()
                    .put(key, key, it)
            }.then()
    }

    override fun removeClient(user: UUID): Mono<Void> {
        val serverUuid = Config.serverUuid
        val key = ClientKey(user)

        return redisOperationsClient
            .opsForHash<ClientKey, ClientValue>()
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
                    redisOperationsClient
                        .opsForHash<ClientKey, ClientValue>()
                        .remove(key, key)
                } else {
                    redisOperationsClient
                        .opsForHash<ClientKey, ClientValue>()
                        .put(key, key, it)
                }
            }
            .then()
    }

    override fun getUserClientInfo(user: UUID) {
        TODO("Not yet implemented")
    }
}