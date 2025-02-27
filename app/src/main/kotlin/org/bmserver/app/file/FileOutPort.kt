package org.bmserver.app.file

import reactor.core.publisher.Mono
import java.net.URL
import java.util.UUID

interface FileOutPort {
    fun getFileUploadPreSignedUrl(file: File): Mono<URL>

    fun getFileUrl(uuid: UUID): Mono<URL>

    fun isExistFile(uuid: UUID): Mono<Boolean>

    fun updateFileToUsed(uuid: UUID): Mono<File>
}
