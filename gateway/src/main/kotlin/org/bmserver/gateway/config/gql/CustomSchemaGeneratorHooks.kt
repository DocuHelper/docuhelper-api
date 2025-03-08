package org.bmserver.gateway.config.gql

import com.expediagroup.graphql.generator.execution.FunctionDataFetcher
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URL
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType

@Component
class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {
    override fun willResolveMonad(type: KType): KType =
        when (type.classifier) {
            Mono::class -> type.arguments.firstOrNull()?.type
            else -> type
        } ?: type

    override fun willGenerateGraphQLType(type: KType): GraphQLType? =
        when (type.classifier as? KClass<*>) {
            UUID::class -> graphqlUUIDType
            URL::class -> graphqlURLType
            Long::class -> graphqlLongType
            else -> null
        }
}

class CustomFunctionDataFetcher(
    target: Any?,
    fn: KFunction<*>,
) : FunctionDataFetcher(target, fn) {
    override fun get(environment: DataFetchingEnvironment): Any? =
        when (val result = super.get(environment)) {
            is Mono<*> -> result.toFuture()
            else -> result
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
