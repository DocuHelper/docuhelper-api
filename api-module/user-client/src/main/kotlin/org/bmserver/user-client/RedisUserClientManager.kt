package org.bmserver.`user-client`

import jakarta.annotation.PostConstruct
import org.bmserver.core.common.Config
import org.bmserver.core.common.notice.UserClientManager
import org.bmserver.core.common.store.MemoryStorage
import org.bmserver.`user-client`.dto.Server
import org.bmserver.`user-client`.dto.User
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class RedisUserClientManager(
    private val memoryStorage: MemoryStorage
) : UserClientManager {
    override fun addClient(userUuid: UUID): Mono<Void> {
        val server = Server(Config.serverUuid)
        val user = User(userUuid)

        return memoryStorage.incrementHash(server, user, 1)
            .flatMap { memoryStorage.incrementHash(user, server, 1) }
            .then()
    }

    override fun removeClient(userUuid: UUID): Mono<Void> {
        val server = Server(Config.serverUuid)
        val user = User(userUuid)

        val task1 = memoryStorage
            .incrementHash(server, user, -1).filter { it == 0L }
            .flatMap { memoryStorage.deleteHashValue(server, user) }
            .then()

        val task2 = memoryStorage.incrementHash(user, server, -1).filter { it == 0L }
            .flatMap { memoryStorage.deleteHashValue(user, server) }
            .then()

        return Mono.`when`(task1, task2).then()
    }

    override fun clearClient(): Mono<Void> {
        val server = Server(Config.serverUuid)

        return memoryStorage.getHashHashKeys(server, User::class.java)
            .flatMap { memoryStorage.deleteHashValue(it, server) }
            .then(memoryStorage.deleteHash(server))
            .then()
    }

    override fun getUserClientInfo(userUuid: UUID): Mono<MutableMap<UUID, Int>> {
        val user = User(userUuid)

        return memoryStorage.getHash(user, Server::class.java, Int::class.java)
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