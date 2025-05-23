package org.bmserver.gateway.config.gql

import com.expediagroup.graphql.server.spring.execution.DefaultSpringGraphQLContextFactory
import com.expediagroup.graphql.server.spring.subscriptions.SpringSubscriptionGraphQLContextFactory
import graphql.GraphQLContext
import io.jsonwebtoken.JwtException
import org.bmserver.core.common.logger
import org.bmserver.core.user.model.User
import org.bmserver.core.user.model.UserRole
import org.bmserver.gateway.config.security.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.socket.WebSocketSession
import java.util.UUID


@Component
class CustomContextFactory(
    private val jwtUtil: JwtUtil,
) : DefaultSpringGraphQLContextFactory(), SpringSubscriptionGraphQLContextFactory {

    @Value("\${dev.default.login.enable}")
    private lateinit var testLoginEnable: String

    @Value("\${dev.default.login.user.uuid}")
    private lateinit var testLoginUUID: String

    @Value("\${dev.default.login.user.email}")
    private lateinit var testLoginEmail: String

    @Value("\${dev.default.login.user.role}")
    private lateinit var testLoginRole: String

    private fun createContextFromToken(jwtToken: String?): GraphQLContext {
        val user = if (testLoginEnable.toBoolean()) {
            User(UUID.fromString(testLoginUUID), testLoginEmail, UserRole.valueOf(testLoginRole))
        } else {
            try {
                jwtToken?.let {
                    val userClaims = jwtUtil.parseJwt(it)
                    val uuid = UUID.fromString(userClaims["sub"] as String)
                    val email = userClaims["email"] as String
                    val role = userClaims["role"] as String
                    User(uuid, email, UserRole.valueOf(role))
                }
            } catch (e: JwtException) {
                logger.error { e }
                null
            }
        }

        val contextBuilder = GraphQLContext.newContext()
        user?.let {
            contextBuilder.put("requestUser", it)
        }
        return contextBuilder.build()
    }


    override suspend fun generateContext(request: ServerRequest): GraphQLContext {
        val jwtTokenByHeader = request.headers().firstHeader("authorization")?.split(" ")?.getOrNull(1)
        val jwtTokenByCookie = request.cookies()["JWT_TOKEN"]?.getOrNull(0)?.value

        val jwtToken = jwtTokenByCookie ?: jwtTokenByHeader

        return createContextFromToken(jwtToken)
    }

    override suspend fun generateContext(session: WebSocketSession, params: Any?): GraphQLContext {
        val jwtTokenByCookie = session.handshakeInfo.cookies["JWT_TOKEN"]?.get(0)?.value
        val jwtTokenByHeader = session.handshakeInfo.headers.getFirst("authorization")?.split(" ")?.getOrNull(1)

        val jwtToken = jwtTokenByCookie ?: jwtTokenByHeader

        return createContextFromToken(jwtToken)
    }

}
