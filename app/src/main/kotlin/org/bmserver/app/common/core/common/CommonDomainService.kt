package org.bmserver.app.common.core.common

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface CommonDomainService<T : BaseDomain> {
    fun create(model: T): Mono<T>

    fun find(query: BaseDomainQuery): Flux<T>

    fun delete(uuid: UUID): Mono<Void>

    fun isExist(uuid: UUID): Mono<Boolean>
}
