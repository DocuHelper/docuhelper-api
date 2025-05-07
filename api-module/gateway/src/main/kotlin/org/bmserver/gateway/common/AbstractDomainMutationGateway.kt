package org.bmserver.gateway.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Mutation
import org.bmserver.core.common.domain.BaseDomain
import org.bmserver.core.common.domain.CommonDomainService
import java.util.UUID

abstract class AbstractDomainMutationGateway<T : BaseDomain>(
    private val commonDomainService: CommonDomainService<T>,
) : Mutation {
    @GraphQLIgnore
    fun create(model: T) = commonDomainService.create(model)

    @GraphQLIgnore
    fun delete(uuid: UUID) = commonDomainService.delete(uuid)
}
