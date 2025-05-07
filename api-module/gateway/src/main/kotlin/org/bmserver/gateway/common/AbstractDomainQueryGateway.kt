package org.bmserver.gateway.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Query
import org.bmserver.core.common.domain.BaseDomain
import org.bmserver.core.common.domain.BaseDomainQuery
import org.bmserver.core.common.domain.CommonDomainService
import reactor.core.publisher.Mono

abstract class AbstractDomainQueryGateway<T : BaseDomain>(
    private val commonDomainService: CommonDomainService<T>,
) : Query {
    @GraphQLIgnore
    fun find(query: BaseDomainQuery): Mono<List<T>> = commonDomainService.find(query).collectList()
}
