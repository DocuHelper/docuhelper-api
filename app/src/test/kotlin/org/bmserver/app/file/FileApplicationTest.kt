package org.bmserver.app.file

import org.bmserver.core.file.FileOutPort
import org.bmserver.core.file.useCase.CreateUploadUrlUseCase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FileApplicationTest {
    @Autowired
    lateinit var fileOutPort: FileOutPort

    @Test
    fun test() {
        val url =
            fileOutPort
                .getFileUploadPreSignedUrl(
                    CreateUploadUrlUseCase(
                        name = "test.txt",
                        extension = "txt",
                        size = 10,
                    ),
                ).block() // 동기적으로 결과를 가져옴 (테스트 환경에서 가능)

        println(url)
    }
}
