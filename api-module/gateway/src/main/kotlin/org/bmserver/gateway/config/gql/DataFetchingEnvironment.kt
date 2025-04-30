package org.bmserver.gateway.config.gql

import graphql.schema.DataFetchingEnvironment
import org.bmserver.core.user.model.User

fun DataFetchingEnvironment.getRequestUser() = this.graphQlContext.get<User>("requestUser")
