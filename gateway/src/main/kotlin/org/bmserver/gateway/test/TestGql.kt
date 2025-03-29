package org.bmserver.app.test

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.bmserver.core.chat.event.ChatAnswer
import org.bmserver.core.common.notice.UserNotifier
import org.bmserver.gateway.config.gql.getRequestUser
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TestGqlQuery(
    private val userNotifier: UserNotifier
) : Query {
    fun testQuery(): String = "test"

    fun test(environment: DataFetchingEnvironment): String {
        userNotifier.send(
            environment.getRequestUser().uuid, ChatAnswer(
                chat = UUID.randomUUID(),
                document = UUID.randomUUID(),
                ask = "",
                answer = ""
            )
        )
            .subscribe()
        return "201"
    }
}

@Component
class TestGqlMutation : Mutation {
    fun testMutation(test: String): String = test
}
