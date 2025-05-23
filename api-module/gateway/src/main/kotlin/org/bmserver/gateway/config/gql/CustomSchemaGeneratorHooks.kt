package org.bmserver.gateway.config.gql

import com.expediagroup.graphql.generator.execution.FunctionDataFetcher
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.bmserver.core.common.logger
import org.bmserver.core.user.model.User
import org.bmserver.gateway.common.AbstractAuthRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.lang.reflect.InvocationTargetException
import java.net.URL
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.callSuspendBy


@Component
class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? =
        when (type.classifier as? KClass<*>) {
            UUID::class -> graphqlUUIDType
            URL::class -> graphqlURLType
            Long::class -> graphqlLongType
            LocalDateTime::class -> graphqlLocalDateTimeType
            else -> null
        }
}

class CustomFunctionDataFetcher(
    val target: Any?,
    val fn: KFunction<*>,
) : FunctionDataFetcher(target, fn) {
    override fun mapParameterToValue(
        param: KParameter,
        environment: DataFetchingEnvironment,
    ): Pair<KParameter, Any?>? {
        val parameterValue = super.mapParameterToValue(param, environment)

        // 유저 인증 적용
        try {
            parameterValue?.second.let {
                when (it) {
                    is AbstractAuthRequest -> it.requestUser = environment.getRequestUser()
                }
            }
        } catch (e: NullPointerException) {
            error("403") // TODO 권한오류
        }

        return parameterValue
    }

    override fun runSuspendingFunction(
        environment: DataFetchingEnvironment,
        parameterValues: Map<KParameter, Any?>
    ): CompletableFuture<Any?> {
        val graphQLContext = environment.graphQlContext

        val scope = CoroutineScope(GraphQLContextElement(graphQLContext)) // 핵심 변경점

        return scope.future {
            try {
                fn.callSuspendBy(parameterValues)
            } catch (exception: InvocationTargetException) {
                throw exception.cause ?: exception
            }
        }
    }


    override fun get(environment: DataFetchingEnvironment): Any? {
        val requestUser = environment.getRequestUser()
        logger.info { "requestUser : $requestUser" }

        return when (val result = super.get(environment)) {
            is Mono<*> -> result
                .contextWrite { it.put(User::class.java, requestUser) }
                .toFuture()

            else -> result
        }
    }
}

@Component
class CustomDataFetcherFactoryProvider : SimpleKotlinDataFetcherFactoryProvider() {
    override fun functionDataFetcherFactory(
        target: Any?,
        kClass: KClass<*>,
        kFunction: KFunction<*>,
    ): DataFetcherFactory<Any?> =
        DataFetcherFactory<Any?> {
            CustomFunctionDataFetcher(
                target = target,
                fn = kFunction,
            )
        }
}

data class GraphQLContextElement(val graphQLContext: graphql.GraphQLContext) :
    AbstractCoroutineContextElement(Key) {
    companion object Key : CoroutineContext.Key<GraphQLContextElement>
}