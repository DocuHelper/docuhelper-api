package org.bmserver.gateway.config.gql

import com.expediagroup.graphql.server.spring.execution.DefaultSpringGraphQLContextFactory
import com.expediagroup.graphql.server.spring.subscriptions.SpringSubscriptionGraphQLContextFactory
import graphql.GraphQLContext
import io.jsonwebtoken.JwtException
import jakarta.annotation.PostConstruct
import org.bmserver.core.common.logger
import org.bmserver.core.user.model.User
import org.bmserver.core.user.model.UserRole
import org.bmserver.gateway.config.security.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.socket.WebSocketSession
import java.util.UUID


@Component
class CustomContextFactory(
    private val jwtUtil: JwtUtil,
    private val environment: Environment,
) : DefaultSpringGraphQLContextFactory(), SpringSubscriptionGraphQLContextFactory {

    @Value("\${dev.default.login.enable}")
    private lateinit var testLoginEnable: String

    @Value("\${dev.default.login.user.uuid}")
    private lateinit var testLoginUUID: String

    @Value("\${dev.default.login.user.email}")
    private lateinit var testLoginEmail: String

    @Value("\${dev.default.login.user.role}")
    private lateinit var testLoginRole: String

    /**
     * 애플리케이션 시작 시 개발용 인증 우회 설정을 검증한다.
     *
     * - prod 프로파일에서 enable=true이면 즉시 애플리케이션 구동을 중단한다.
     * - 그 외 프로파일에서 enable=true이면 보안 경고 로그를 출력한다.
     */
    @PostConstruct
    fun validateDevLoginConfig() {
        if (testLoginEnable.toBoolean()) {
            if (environment.activeProfiles.contains("prod")) {
                throw IllegalStateException(
                    "[SECURITY] dev.default.login.enable=true는 prod 프로파일에서 절대 허용되지 않습니다. " +
                        "이 기능은 JWT 인증을 완전히 우회합니다. 즉시 false로 변경하십시오.",
                )
            }
            logger.warn {
                "[SECURITY WARNING] dev.default.login이 활성화되어 있습니다. " +
                    "JWT 인증이 우회되고 있습니다! 이 설정은 로컬 개발 환경에서만 사용해야 하며, " +
                    "절대로 프로덕션에 배포해서는 안 됩니다."
            }
        }
    }

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
