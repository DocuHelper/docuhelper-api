package org.bmserver.gateway.config.gql

import graphql.GraphQLError
import graphql.GraphqlErrorException
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class ErrorHandler : DataFetcherExceptionHandler {

    override fun handleException(handlerParameters: DataFetcherExceptionHandlerParameters?): CompletableFuture<DataFetcherExceptionHandlerResult> {
        val exception = handlerParameters?.exception
        val sourceLocation = handlerParameters?.sourceLocation
        val path = handlerParameters?.path

        val error: GraphQLError = when (exception) {
            else ->
                GraphqlErrorException.newErrorException()
                    .cause(exception)
                    .message(exception?.message ?: "Err?")
                    .sourceLocation(sourceLocation)
                    .path(path?.toList())
                    .build()
        }

        exception?.printStackTrace()
        val exceptionResult = DataFetcherExceptionHandlerResult.newResult().error(error).build()
        return CompletableFuture.completedFuture(exceptionResult)    }
}