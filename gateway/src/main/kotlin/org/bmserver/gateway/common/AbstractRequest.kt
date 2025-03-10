package org.bmserver.gateway.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bmserver.gateway.config.security.User
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import java.util.UUID

abstract class AbstractRequest {
    @GraphQLIgnore
    lateinit var requestUser: UUID

    init {
        ReactiveSecurityContextHolder
            .getContext()
            .map { securityContext ->
                val user = securityContext.authentication.principal as User
                println(user)
                this.requestUser = user.uuid
                user
            }.subscribe { println(it) }
    }
}
