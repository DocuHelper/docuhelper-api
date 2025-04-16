package org.bmserver.redis

import jakarta.annotation.PostConstruct
import org.bmserver.core.common.Config
import org.bmserver.core.common.notice.UserClientManager
import org.bmserver.redis.dto.Server
import org.bmserver.redis.dto.User
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class RedisUserClientManager(
    private val serverRedisTemplate: ReactiveRedisTemplate<Server, Int>,
    private val userRedisTemplate: ReactiveRedisTemplate<User, Int>
) : UserClientManager {
    override fun addClient(userUuid: UUID): Mono<Void> {
        val server = Server(Config.serverUuid)
        val user = User(userUuid)

        return serverRedisTemplate.opsForHash<User, Int>()
            .increment(server, user, 1)
            .flatMap { userRedisTemplate.opsForHash<Server, Int>().increment(user, server, 1) }
            .then()
    }

    override fun removeClient(userUuid: UUID): Mono<Void> {
        val server = Server(Config.serverUuid)
        val user = User(userUuid)

        val task1 = serverRedisTemplate.opsForHash<User, Int>()
            .increment(server, user, -1).filter { it == 0L }
            .flatMap { serverRedisTemplate.opsForHash<User, Int>().remove(server, user) }
            .then()

        val task2 = userRedisTemplate.opsForHash<Server, Int>().increment(user, server, -1).filter { it == 0L }
            .flatMap { userRedisTemplate.opsForHash<Server, Int>().remove(user, server) }
            .then()

        return Mono.`when`(task1, task2).then()
    }

    override fun clearClient(): Mono<Void> {
        val server = Server(Config.serverUuid)

        return serverRedisTemplate.opsForHash<User, Int>()
            .keys(server)
            .flatMap { userRedisTemplate.opsForHash<Server, Int>().remove(it, server) }
            .then(serverRedisTemplate.opsForHash<User, Int>()
                .delete(server))
            .then()
    }

    override fun getUserClientInfo(userUuid: UUID): Mono<MutableMap<UUID, Int>> {
        val user = User(userUuid)

        return userRedisTemplate.opsForHash<Server, Int>()
            .entries(user)
            .map { it.key.server to it.value }
            .collectList()
            .map { it.toMap().toMutableMap() }
    }

    @PostConstruct
    fun applicationShutdownHook() {
        Runtime.getRuntime().addShutdownHook(Thread {
            clearClient().subscribe()
        })
    }
}