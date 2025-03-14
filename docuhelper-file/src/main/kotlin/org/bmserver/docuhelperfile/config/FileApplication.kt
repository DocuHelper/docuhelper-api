package org.bmserver.docuhelperfile.config

import org.bmserver.core.file.File
import org.bmserver.core.file.FileOutPort
import org.bmserver.core.file.response.UploadUrl
import org.bmserver.core.file.useCase.CreateUploadUrlUseCase
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.net.URL
import java.util.UUID

@Service
class FileApplication(
    private val docuHelperFileClient: DocuHelperFileClient,
) : FileOutPort {
    override fun getFileUploadPreSignedUrl(useCase: CreateUploadUrlUseCase): Mono<UploadUrl> =
        docuHelperFileClient.getUploadPreSignedUrl(useCase)

    override fun getFileUrl(uuid: UUID): Mono<URL> = docuHelperFileClient.getFileUrl(uuid)

    override fun isExistFile(uuid: UUID): Mono<Boolean> {
        return Mono.just(false) // TODO
    }

    override fun updateFileToUsed(uuid: UUID): Mono<File> = docuHelperFileClient.updateFileToUsed(uuid)
}
