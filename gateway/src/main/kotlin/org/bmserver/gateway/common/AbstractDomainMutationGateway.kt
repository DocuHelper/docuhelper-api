package org.bmserver.gateway.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Mutation
import org.bmserver.core.common.BaseDomain
import org.bmserver.core.common.CommonDomainService
import reactor.core.publisher.Mono

abstract class AbstractDomainMutationGateway<T : BaseDomain>(
    private val commonDomainService: CommonDomainService<T>,
) : Mutation {
    @GraphQLIgnore
    fun create(model: T): Mono<T> = commonDomainService.create(model)
}
