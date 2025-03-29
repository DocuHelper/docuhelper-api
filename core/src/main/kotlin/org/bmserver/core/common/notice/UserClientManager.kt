package org.bmserver.core.common.notice

import reactor.core.publisher.Mono
import java.util.UUID

// 유저 클라이언트 관리
interface UserClientManager {

    fun addClient(user: UUID): Mono<Void>

    fun removeClient(user: UUID): Mono<Void>

    fun getUserClientInfo(user: UUID): Mono<MutableMap<UUID, Int>>
}