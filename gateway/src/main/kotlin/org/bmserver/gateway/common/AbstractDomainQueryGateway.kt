package org.bmserver.gateway.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Query
import org.bmserver.core.common.BaseDomain
import org.bmserver.core.common.BaseDomainQuery
import org.bmserver.core.common.CommonDomainService
import reactor.core.publisher.Flux

abstract class AbstractDomainQueryGateway<T : BaseDomain>(
    private val commonDomainService: CommonDomainService<T>,
) : Query {
    @GraphQLIgnore
    fun find(query: BaseDomainQuery): Flux<T> = commonDomainService.find(query)
}
