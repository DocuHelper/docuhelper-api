package org.bmserver.app.test.run

import com.fasterxml.jackson.databind.ObjectMapper
import org.bmserver.app.test.TestContentEls
import org.bmserver.app.test.TestQaElsRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.StringQuery
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.util.concurrent.ConcurrentHashMap

@Service
class Benchmark(
    private val testQaElsRepository: TestQaElsRepository,
    private val template: ReactiveElasticsearchOperations,
    private val om: ObjectMapper
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        benchmark_findByTokenAndKnn(
            knnBoost = 0.0,
            searchDocumentCount = 20,
            tokenBoost = 1.0
        )
            .subscribe()
    }

    fun queryByVectorNoneKnn(
        vector: List<Float>,
        searchDocumentCount: Int
    ): Flux<TestContentEls> {
        val json = om.writeValueAsString(
            mapOf(
                "script_score" to mapOf(
                    "query" to mapOf(
                        "match_all" to mapOf<String, String>()
                    ),
                    "script" to mapOf(
                        "source" to "cosineSimilarity(params.query_vector, '${if (vector.size == 1536) "embed_openai_3_small" else "embed_openai_3_large"}') + 1.0",
                        "params" to mapOf(
                            "query_vector" to vector
                        )
                    ),
                )
            )
        )

        val query = StringQuery(json)
            .also { it.setMaxResults(searchDocumentCount) }

        return template.search(query, TestContentEls::class.java)
            .map { it.content }
    }

    fun queryByVectorKnn(
        vector: List<Float>,
        searchDocumentCount: Int
    ): Flux<TestContentEls> {
        val json = om.writeValueAsString(
            mapOf(
                "knn" to mapOf(
                    "field" to if (vector.size == 1536) "embed_openai_3_small" else "embed_openai_3_large",
                    "query_vector" to vector
                )
            )
        )

        val query = StringQuery(json)
            .also { it.setMaxResults(searchDocumentCount) }

        return template.search(query, TestContentEls::class.java)
            .map { it.content }
    }

    fun queryByToken(
        q: String,
        searchDocumentCount: Int
    ): Flux<TestContentEls> {
        val json = om.writeValueAsString(
            mapOf(
                "match" to mapOf(
                    "content.nori" to mapOf(
                        "query" to q,
                    )
                )
            ),
        )

        val query = StringQuery(json)
            .also { it.setMaxResults(searchDocumentCount) }

        return template.search(query, TestContentEls::class.java)
            .map { it.content }
    }

    fun queryByKnnAndToken(
        q: String,
        tokenBoost: Double,
        qVector: List<Float>,
        knnBoost: Double,
        searchDocumentCount: Int
    ): Flux<TestContentEls> {
        val json = om.writeValueAsString(
            mapOf(
                "bool" to mapOf(
                    "should" to listOf(
                        mapOf(
                            "match" to mapOf(
                                "content.nori" to mapOf(
                                    "query" to q,
                                    "boost" to tokenBoost
                                )
                            )
                        ),
                        mapOf(
                            "knn" to mapOf(
                                "field" to if (qVector.size == 1536) "embed_openai_3_small" else "embed_openai_3_large",
                                "query_vector" to qVector,
                                "boost" to knnBoost
                            )
                        )
                    )
                )
            )
        )

        val query = StringQuery(json)
            .also { it.setMaxResults(searchDocumentCount) }

        return template.search(query, TestContentEls::class.java)
            .map { it.content }
    }


    fun benchmark_findByVector_none_knn(searchDocumentCount: Int): Mono<Void> {

        var total = 0
        val result = ConcurrentHashMap<Int, Int>()

        for (i: Int in -1..<searchDocumentCount) {
            result.put(i, 0)
        }

        val qaUuids = testQaElsRepository.findAll()
            .map { it.uuid }
            .collectList()
            .block()

        return qaUuids!!.toFlux().flatMap {

            testQaElsRepository.findById(it)
                .flatMap { currentQa ->
                    queryByVectorNoneKnn(
                        vector = currentQa.embedOpenai3Small!!,
                        searchDocumentCount = searchDocumentCount
                    )
                        .index()
                        .filter { it.t2.uuid == currentQa.contentId }
                        .next()
                        .map { it.t1.toInt() }
                        .defaultIfEmpty(-1)
                }
        }

            .doOnNext {
                result[it] = result[it]!! + 1
                total++;
                if (total % 1000 == 0) {
                    println("진행 : ${total}")
                }
            }
            .doOnTerminate {
                println("========== Map ==========")
                result.map {
                    println("${it.key} \t : \t ${it.value}")
                }

                println("===========================")
                for (i: Int in 0..<searchDocumentCount) {
                    var sum = 0
                    for (l: Int in 0..i) {
                        sum += result[l]!!
                    }
                    println("${i + 1} \t : \t $sum")
                }

            }
            .then()

    }


    fun benchmark_findByVector_knn(searchDocumentCount: Int): Mono<Void> {

        var total = 0
        val result = ConcurrentHashMap<Int, Int>()

        for (i: Int in -1..<searchDocumentCount) {
            result[i] = 0
        }

        return testQaElsRepository.findAll()
            .map { it.uuid }
            .collectList()
            .block()!!
            .toFlux()
            .flatMap { testQaElsRepository.findById(it) }
            .flatMap { currentQa ->
                queryByVectorKnn(
                    vector = currentQa.embedOpenai3Small!!,
                    searchDocumentCount = searchDocumentCount
                )
                    .index()
                    .filter { it.t2.uuid == currentQa.contentId }
                    .next()
                    .map { it.t1.toInt() }
                    .defaultIfEmpty(-1)
            }
            .doOnNext {
                result[it] = result[it]!! + 1

                total++
                if (total % 1000 == 0) {
                    print("\r진행률: ${total}")
                    System.out.flush()
                }
            }
            .doOnTerminate {
                println("")
                println("searchDocumentCount : ${searchDocumentCount}")
                println("Total: ${total}")

                for (i: Int in 0..<searchDocumentCount) {
                    var sum = 0
                    for (l: Int in 0..i) {
                        sum += result[l]!!
                    }
                    println("${i + 1} : ${sum}")
                }

            }
            .then()
    }


    fun benchmark_findByToken(searchDocumentCount: Int): Mono<Void> {

        var total = 0
        val result = ConcurrentHashMap<Int, Int>()

        for (i: Int in -1..<searchDocumentCount) {
            result[i] = 0
        }

        return testQaElsRepository.findAll()
            .flatMap { currentQa ->
                queryByToken(
                    q = currentQa.q,
                    searchDocumentCount = searchDocumentCount
                )
                    .index()
                    .filter { it.t2.uuid == currentQa.contentId }
                    .next()
                    .map { it.t1.toInt() }
                    .defaultIfEmpty(-1)
            }
            .doOnNext {
                result[it] = result[it]!! + 1

                total++
                if (total % 1000 == 0) {
                    print("\r진행률: ${total}")
                    System.out.flush()
                }
            }
            .doOnTerminate {
                println("")
                println("searchDocumentCount : ${searchDocumentCount}")
                println("Total: ${total}")

                for (i: Int in 0..<searchDocumentCount) {
                    var sum = 0
                    for (l: Int in 0..i) {
                        sum += result[l]!!
                    }
                    println("${i + 1} : ${sum}")
                }

            }
            .then()
    }

    fun benchmark_findByTokenAndKnn(
        searchDocumentCount: Int,
        tokenBoost: Double,
        knnBoost: Double
    ): Mono<Void> {

        var total = 0
        val result = ConcurrentHashMap<Int, Int>()

        for (i: Int in -1..<searchDocumentCount) {
            result[i] = 0
        }

        return testQaElsRepository.findAll()
            .map { it.uuid }
            .collectList()
            .block()!!
            .toFlux()
            .flatMap {
                testQaElsRepository.findById(it)
            }
            .flatMap { currentQa ->
                queryByKnnAndToken(
                    q = currentQa.q,
                    tokenBoost = tokenBoost,
                    qVector = currentQa.embedOpenai3Small!!,
                    knnBoost = knnBoost,
                    searchDocumentCount = searchDocumentCount
                )
                    .index()
                    .filter { it.t2.uuid == currentQa.contentId }
                    .next()
                    .map { it.t1.toInt() }
                    .defaultIfEmpty(-1)
            }
            .doOnNext {
                result[it] = result[it]!! + 1

                total++
                if (total % 1000 == 0) {
                    print("\r진행률: ${total}")
                    System.out.flush()
                }
            }
            .doOnTerminate {
                println("")
                println("tokenBost : ${tokenBoost}")
                println("knnBost : ${knnBoost}")
                println("searchDocumentCount : ${searchDocumentCount}")
                println("Total: ${total}")

                for (i: Int in 0..<searchDocumentCount) {
                    var sum = 0
                    for (l: Int in 0..i) {
                        sum += result[l]!!
                    }
                    println("${i + 1} : ${sum}")
                }

            }
            .then()
    }

}


















