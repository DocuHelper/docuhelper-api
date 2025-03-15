package org.bmserver.gateway.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Query
import kotlinx.coroutines.reactor.awaitSingle
import org.bmserver.core.common.BaseDomain
import org.bmserver.core.common.BaseDomainQuery
import org.bmserver.core.common.CommonDomainService

abstract class AbstractDomainQueryGateway<T : BaseDomain>(
    private val commonDomainService: CommonDomainService<T>,
) : Query {
    @GraphQLIgnore
    suspend fun find(query: BaseDomainQuery): List<T> = commonDomainService.find(query).collectList().awaitSingle()
}
