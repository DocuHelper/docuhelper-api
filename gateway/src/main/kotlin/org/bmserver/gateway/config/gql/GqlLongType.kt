package org.bmserver.gateway.config.gql

import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.IntValue
import graphql.language.Value
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import java.math.BigInteger
import java.util.Locale

val graphqlLongType =
    GraphQLScalarType
        .newScalar()
        .name("Long")
        .description("A custom scalar that handles 64-bit integer (Kotlin Long).")
        .coercing(LongCoercing)
        .build()

object LongCoercing : Coercing<Long, Long> {
    override fun parseValue(
        input: Any,
        graphQLContext: GraphQLContext,
        locale: Locale,
    ): Long =
        when (input) {
            is Number -> input.toLong()
            is String -> input.toLongOrNull() ?: throw CoercingParseValueException("Cannot parse $input to Long.")
            else -> throw CoercingParseValueException("Expected a number or numeric string but was $input")
        }

    override fun parseLiteral(
        input: Value<*>,
        variables: CoercedVariables,
        graphQLContext: GraphQLContext,
        locale: Locale,
    ): Long {
        if (input !is IntValue) {
            throw CoercingParseLiteralException("Expected AST type 'IntValue' but was $input")
        }

        val bigValue: BigInteger = input.value
        return bigValue.toLong()
    }

    override fun serialize(
        dataFetcherResult: Any,
        graphQLContext: GraphQLContext,
        locale: Locale,
    ): Long =
        when (dataFetcherResult) {
            is Number -> dataFetcherResult.toLong()
            else -> throw CoercingSerializeException("Data fetcher result $dataFetcherResult cannot be serialized to a Long.")
        }
}
