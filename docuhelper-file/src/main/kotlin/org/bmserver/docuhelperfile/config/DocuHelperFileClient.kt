package org.bmserver.docuhelperfile.config

import org.bmserver.core.file.File
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.net.URL
import java.util.UUID

@Component
class DocuHelperFileClient {
    @Value("\${docuhelper.file.endpoint}")
    private lateinit var docuhelperFileEndpoint: String

    fun getUploadPreSignedUrl(file: File): Mono<URL> =
        getClient()
            .post()
            .uri("/file")
            .bodyValue(file)
            .retrieve()
            .bodyToMono(URL::class.java)

    fun getFileUrl(uuid: UUID): Mono<URL> =
        getClient()
            .get()
            .uri("/file/$uuid")
            .retrieve()
            .bodyToMono(URL::class.java)

    fun updateFileToUsed(uuid: UUID) =
        getClient()
            .patch()
            .uri("/file/$uuid/is-used")
            .retrieve()
            .bodyToMono(File::class.java)

    private fun getClient(): WebClient =
        WebClient
            .builder()
            .baseUrl(docuhelperFileEndpoint)
            .build()
}
