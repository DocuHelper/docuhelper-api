package org.bmserver.gateway.config.gql

import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.StringValue
import graphql.language.Value
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import java.time.LocalDateTime
import java.util.Locale

val graphqlLocalDateTimeType =
    GraphQLScalarType
        .newScalar()
        .name("LocalDateTime")
        .description("A custom scalar LocalDateTime.")
        .coercing(LocalDateTimeCoercing)
        .build()

object LocalDateTimeCoercing : Coercing<LocalDateTime, String> {
    override fun parseValue(
        input: Any,
        graphQLContext: GraphQLContext,
        locale: Locale,
    ): LocalDateTime =
        when (input) {
            is String -> LocalDateTime.parse(input)
            else -> throw CoercingParseValueException("LocalDateTime")
        }

    override fun parseLiteral(
        input: Value<*>,
        variables: CoercedVariables,
        graphQLContext: GraphQLContext,
        locale: Locale,
    ): LocalDateTime {
        if (input !is StringValue) {
            throw CoercingParseLiteralException("Expected AST type 'IntValue' but was $input")
        }

        return LocalDateTime.parse(input.value)
    }

    override fun serialize(
        dataFetcherResult: Any,
        graphQLContext: GraphQLContext,
        locale: Locale,
    ): String =
        when (dataFetcherResult) {
            is LocalDateTime -> dataFetcherResult.toString()
            else -> throw CoercingSerializeException("Data fetcher result $dataFetcherResult cannot be serialized to a LocalDateTime.")
        }
}
