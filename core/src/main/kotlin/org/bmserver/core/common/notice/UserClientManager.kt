package org.bmserver.core.common.notice

import reactor.core.publisher.Mono
import java.util.UUID

// 유저 클라이언트 관리
interface UserClientManager {

    fun addClient(userUuid: UUID): Mono<Void>

    fun removeClient(userUuid: UUID): Mono<Void>

    fun clearClient(): Mono<Void>

    fun getUserClientInfo(userUuid: UUID): Mono<MutableMap<UUID, Int>>
}