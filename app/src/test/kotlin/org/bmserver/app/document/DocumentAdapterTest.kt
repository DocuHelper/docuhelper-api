package org.bmserver.app.document

import org.bmserver.core.document.DocumentOutPort
import org.bmserver.core.document.model.DocumentQuery
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DocumentAdapterTest {
    @Autowired
    lateinit var documentOutPort: DocumentOutPort

    @Test
    fun test() {
        val result =
            documentOutPort
                .find(
                    DocumentQuery(
                        uuid = null,
                        name = "테스트",
                        state = null,
                        owner = null,
                    ),
                ).blockFirst()

        println(result)
    }
}
