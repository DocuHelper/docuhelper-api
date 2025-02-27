package org.bmserver.docuhelperapi.domain.document

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.bmserver.docuhelperapi.common.core.document.model.Document
import org.bmserver.docuhelperapi.common.core.document.model.DocumentState
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class DocumentDataAdapterTest {
    @Autowired
    lateinit var documentRepository: DocumentRepository

    @Test
    fun test() {
        runTest {
            println("!!!!!!!!!!!!!")
            documentRepository
                .save(
                    Document(
                        name = "asdf",
                        state = DocumentState.PARSING,
                        file = UUID.randomUUID(),
                        owner = UUID.randomUUID(),
                    ),
                ).subscribe {
                    println(it)
                }
            delay(1000)
        }
    }
}
