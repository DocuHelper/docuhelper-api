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
import java.net.URI
import java.net.URL
import java.util.Locale

val graphqlURLType =
    GraphQLScalarType
        .newScalar()
        .name("URL")
        .coercing(URLCoercing)
        .build()

object URLCoercing : Coercing<URL, String> {
    override fun parseValue(
        input: Any,
        graphQLContext: GraphQLContext,
        locale: Locale,
    ): URL =
        runCatching {
            URI.create(serialize(input, graphQLContext, locale)).toURL()
        }.getOrElse {
            throw CoercingParseValueException("Expected valid URL but was $input")
        }

    override fun parseLiteral(
        input: Value<*>,
        variables: CoercedVariables,
        graphQLContext: GraphQLContext,
        locale: Locale,
    ): URL {
        val urlString = (input as? StringValue)?.value
        return runCatching {
            URI.create(urlString).toURL()
        }.getOrElse {
            throw CoercingParseLiteralException("Expected valid url literal but was $urlString")
        }
    }

    override fun serialize(
        dataFetcherResult: Any,
        graphQLContext: GraphQLContext,
        locale: Locale,
    ): String =
        runCatching {
            dataFetcherResult.toString()
        }.getOrElse {
            throw CoercingSerializeException("Data fetcher result $dataFetcherResult cannot be serialized to a String")
        }
}
