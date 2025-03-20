package org.bmserver.gateway.config.gql

import com.expediagroup.graphql.server.spring.execution.DefaultSpringGraphQLContextFactory
import graphql.GraphQLContext
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtException
import org.bmserver.gateway.config.security.JwtUtil
import org.bmserver.gateway.config.security.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import java.util.UUID

private val logger = KotlinLogging.logger { }

@Component
class CustomContextFactory(
    private val jwtUtil: JwtUtil,
) : DefaultSpringGraphQLContextFactory() {

    @Value("\${dev.default.login.enable}")
    private lateinit var testLoginEnable: String

    @Value("\${dev.default.login.user.uuid}")
    private lateinit var testLoginUUID: String

    @Value("\${dev.default.login.user.email}")
    private lateinit var testLoginEmail: String


    override suspend fun generateContext(request: ServerRequest): GraphQLContext {
        val jwtToken = request.cookies()["JWT_TOKEN"]?.get(0)?.value

        val user = if (testLoginEnable.toBoolean()) {
            User(UUID.fromString(testLoginUUID), testLoginEmail)
        } else {
            try {
                jwtToken?.let {
                    val userClaims = jwtUtil.parseJwt(it)
                    val uuid = UUID.fromString(userClaims["sub"] as String)
                    val email = userClaims["email"] as String

                    User(uuid, email)
                }
            } catch (e: JwtException) {
                logger.error { e }
                null
            }
        }

        val context =
            GraphQLContext
                .newContext()

        user?.let {
            context.put("requestUser", it)
        }
        return context.build()
    }
}
