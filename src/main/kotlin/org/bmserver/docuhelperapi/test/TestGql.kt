package org.bmserver.docuhelperapi.test

import com.expediagroup.graphql.generator.annotations.GraphQLType
import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component


@Component
class TestGqlQuery : Query {
    fun testQuery(): String = "test"
}

@Component
class TestGqlMutation : Mutation {
    fun testMutation(test:String): String = test
}
