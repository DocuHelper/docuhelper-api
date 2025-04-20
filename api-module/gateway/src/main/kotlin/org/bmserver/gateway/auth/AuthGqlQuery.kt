package org.bmserver.gateway.auth

import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.bmserver.gateway.config.gql.getRequestUser
import org.springframework.stereotype.Component

@Component
class AuthGqlQuery : Query {
    fun loginState(environment: DataFetchingEnvironment) = environment.getRequestUser() ?: null
}
