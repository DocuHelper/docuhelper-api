package org.bmserver.app.test.run

import com.fasterxml.jackson.databind.ObjectMapper
import org.bmserver.app.test.BatchEmbeddingResponse
import org.bmserver.app.test.TestContent
import org.bmserver.app.test.TestContentEls
import org.bmserver.app.test.TestContentElsRepository
import org.bmserver.app.test.TestContentRepository
import org.bmserver.app.test.TestQA
import org.bmserver.app.test.TestQAEls
import org.bmserver.app.test.TestQARepository
import org.bmserver.app.test.TestQaElsRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.io.File
import java.util.UUID

@Service
class LoadData(
    val testContentRepository: TestContentRepository,
    val testContentElsRepository: TestContentElsRepository,
    val testQARepository: TestQARepository,
    val testQaElsRepository: TestQaElsRepository,
    val om:ObjectMapper
) {

    fun updateVectorContent() {
        val file =
            File("/Users/bm/Desktop/Project/intellijProjects/docuhelper/docuhelper-api/api-module/app/src/main/resources/data/test_content/test_content_embed_3_large.jsonl")
        val buffer = file.bufferedReader()

        buffer.useLines {
            it.forEach {
                val currentObj = om.readValue(it, BatchEmbeddingResponse::class.java)
                testContentRepository.findById(UUID.fromString(currentObj.custom_id)).flatMap {
                    it.embedOpenai3Large = currentObj.response.body.data.first().embedding

                    testContentRepository.save(it)
                }.block()
            }
        }

        println("")
    }

    fun loadAiHubeDataByDB() {
        val file =
            File("/Users/bm/Desktop/Project/intellijProjects/docuhelper/docuhelper-api/api-module/app/src/main/resources/data/ai_hub_original_data.json")
        val result = om.readValue(file.inputStream(), Root::class.java)

        result.data.map {
            it.paragraphs.map { data ->
                testContentRepository.save(TestContent(content = data.context))
                    .flatMap { testContent ->
                        Mono.`when`(data.qas.map {
                            testQARepository.save(
                                TestQA(
                                    contentId = testContent.uuid,
                                    q = it.question,
                                    a = it.answers.first().text,
                                )
                            )
                        })
                    }.block()

            }
        }

        println("")
    }

    fun loadQaElsFromRdb() {
        var count = 1
        testQARepository.findAll()
            .flatMap {
                testQaElsRepository.save(
                    TestQAEls(
                        uuid = it.uuid,
                        contentId = it.contentId,
                        q = it.q,
                        a = it.a,
                        embedOpenai3Small = it.embedOpenai3Small,
                        embedOpenai3Large = it.embedOpenai3Large
                    )
                )
            }
            .doOnNext {
                println(count)
                count++
            }
            .doOnTerminate { println("!!!!!!!!!!!!!!!!!END!!!!!!!!!!!!!!!!!!!!") }
            .subscribe()
    }


    fun loadContentElsFromRdb() {
        var count = 1
        testContentRepository.findAll()
            .flatMap {
                testContentElsRepository.save(
                    TestContentEls(
                        uuid = it.uuid,
                        content = it.content,
                        embedOpenai3Small = it.embedOpenai3Small,
                        embedOpenai3Large = it.embedOpenai3Large
                    )
                )
            }
            .doOnNext {
                println(count)
                count++
            }
            .subscribe()
    }

    fun updateVectorQa() {
        val parts = listOf(1, 2, 3, 4, 5)

        parts.forEach { part ->

            val file =
                File("/Users/bm/Desktop/Project/intellijProjects/docuhelper/docuhelper-api/api-module/app/src/main/resources/data/test_qa/test_qa_part${part}_embed_3_large.jsonl")

            val buffer = file.bufferedReader()

            buffer.useLines {
                it.forEach {
                    val currentObj = om.readValue(it, BatchEmbeddingResponse::class.java)
                    testQARepository.findById(UUID.fromString(currentObj.custom_id)).flatMap {
                        it.embedOpenai3Large = currentObj.response.body.data.first().embedding

                        testQARepository.save(it)
                    }.block()
                }
            }
        }

        println("")
    }

}

class Root(
    val creator: String,
    val version: Int,
    val data: List<Data>
)

class Data(
    val title: String,
    val paragraphs: List<Paragraphs>,
    val source: String,
)

class Paragraphs(
    val context: String,
    val qas: List<QAS>

)

class QAS(
    val question: String,
    val answers: List<Answer>,
)

class Answer(
    val answer_start: Int?,
    val text: String
)