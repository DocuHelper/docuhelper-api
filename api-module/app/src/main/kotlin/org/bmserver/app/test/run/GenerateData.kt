package org.bmserver.app.test.run

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.bmserver.app.test.BatchData
import org.springframework.stereotype.Service
import java.io.BufferedWriter
import java.io.File

@Service
class GenerateData(
    private val om: ObjectMapper
) {
    fun getQaOpenAiBatchFileByOriginalData() {
        val file =
            File("/Users/bm/Desktop/Project/intellijProjects/docuhelper/docuhelper-api/api-module/app/src/main/resources/data/test_qa/test_qa_original.json")
        val json = om.readValue(file.inputStream(), object : TypeReference<LinkedHashMap<String, Any>>() {})
        val testContent = json["test_qa"] as ArrayList<LinkedHashMap<String, String>>

        val batchSize = 50000
        var partIndex = 1
        var writer: BufferedWriter? = null

        testContent.map { item ->
            BatchData(
                item["uuid"]!!,
                input = item["q"]!!
            )
        }.forEachIndexed { index, batchData ->
            if (index % batchSize == 0) {
                // close previous writer if exists
                writer?.close()
                // open new part file
                val outFile =
                    File("/Users/bm/Desktop/Project/intellijProjects/docuhelper/docuhelper-api/api-module/app/src/main/resources/data/test_qa/test_qa_part${partIndex}_batch_3_large.jsonl")
                writer = outFile.bufferedWriter()
                partIndex++
            }
            writer!!.write(om.writeValueAsString(batchData))
            writer!!.newLine()
            println(index)
        }
        // close the last writer
        writer?.close()
    }
}