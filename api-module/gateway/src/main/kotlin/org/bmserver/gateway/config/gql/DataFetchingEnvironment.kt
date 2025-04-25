package org.bmserver.gateway.config.gql

import graphql.schema.DataFetchingEnvironment
import org.bmserver.gateway.config.security.User

fun DataFetchingEnvironment.getRequestUser() = this.graphQlContext.get<User>("requestUser")
