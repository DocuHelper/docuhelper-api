package org.bmserver.gateway.test

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.expediagroup.graphql.server.operations.Query
import graphql.GraphQLContext
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class TestGqlQuery : Query {
    fun test(request: TestRequest): List<TestResponse> {
        return listOf(
            TestResponse(request.value),
            TestResponse(request.value + 1),
            TestResponse(request.value + 2)
        )
    }
}

@Component
class TestResponseDataLoader : KotlinDataLoader<String, String> {
    override val dataLoaderName: String
        get() = "TestResponseDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<String, String> {
        return DataLoaderFactory.newDataLoader<String, String>(
            { strings, env ->
                println("getDataLoader")
                CompletableFuture.supplyAsync {
                    println("getDataLoader : supplyAsync")
                    println("string : " + strings)
                    strings.map { it+"test" }
                }
            },
            DataLoaderOptions.newOptions().setCachingEnabled(false)
                .setBatchLoaderContextProvider { graphQLContext }
        )
    }

}

class TestRequest(
    val value: Int
)

class TestResponse(
    val value: Int,
) {
    fun test(environment: DataFetchingEnvironment): CompletableFuture<String> {


        return environment.getValueFromDataLoader<String, String>("TestResponseDataLoader", value.toString())
    }
}