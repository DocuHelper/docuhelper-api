package org.bmserver.gateway.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bmserver.gateway.config.security.User

abstract class AbstractRequest {
    @GraphQLIgnore
    lateinit var requestUser: User
}
