package org.bmserver.gateway.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bmserver.core.user.model.User

abstract class AbstractAuthRequest {
    @GraphQLIgnore
    lateinit var requestUser: User
}
