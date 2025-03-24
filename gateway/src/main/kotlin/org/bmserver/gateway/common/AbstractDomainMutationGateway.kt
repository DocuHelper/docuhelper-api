package org.bmserver.gateway.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Mutation
import kotlinx.coroutines.reactor.awaitSingle
import org.bmserver.core.common.domain.BaseDomain
import org.bmserver.core.common.domain.CommonDomainService

abstract class AbstractDomainMutationGateway<T : BaseDomain>(
    private val commonDomainService: CommonDomainService<T>,
) : Mutation {
    @GraphQLIgnore
    suspend fun create(model: T): T = commonDomainService.create(model)
        .doOnError{
            it.printStackTrace() //TODO
        }
        .awaitSingle()
}
