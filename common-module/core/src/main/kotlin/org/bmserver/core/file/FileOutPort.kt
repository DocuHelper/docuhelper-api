package org.bmserver.core.file

import org.bmserver.core.file.response.UploadUrl
import org.bmserver.core.file.useCase.CreateUploadUrlUseCase
import reactor.core.publisher.Mono
import java.net.URL
import java.util.UUID

interface FileOutPort {
    fun getFileUploadPreSignedUrl(useCase: CreateUploadUrlUseCase): Mono<UploadUrl>

    fun getFileUrl(uuid: UUID): Mono<URL>

    fun isExistFile(uuid: UUID): Mono<Boolean>

    fun updateFileToUsed(uuid: UUID): Mono<File>
}
