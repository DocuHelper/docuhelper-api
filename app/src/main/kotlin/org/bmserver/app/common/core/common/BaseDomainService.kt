package org.bmserver.app.common.core.common

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

abstract class BaseDomainService<T : BaseDomain>(
    private val baseDomainRepository: BaseDomainRepository<T>,
    private val baseDomainQueryRepository: BaseDomainQueryRepository<T>,
) : CommonDomainService<T> {
    override fun create(model: T): Mono<T> = baseDomainRepository.save(model)

    override fun find(query: BaseDomainQuery): Flux<T> = baseDomainQueryRepository.searchQuery(query)

    override fun delete(uuid: UUID): Mono<Void> = baseDomainRepository.deleteById(uuid)

    override fun isExist(uuid: UUID): Mono<Boolean> = baseDomainRepository.existsById(uuid)
}
