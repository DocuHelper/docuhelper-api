package org.bmserver.gateway.file

import com.expediagroup.graphql.server.operations.Mutation
import org.bmserver.core.file.FileOutPort
import org.bmserver.gateway.file.request.CreateUploadUrlRequest
import org.springframework.stereotype.Component

@Component
class FileGqlMutation(
    private val fileOutPort: FileOutPort,
) : Mutation {
    fun uploadFileUrl(fileInfo: CreateUploadUrlRequest) = fileOutPort.getFileUploadPreSignedUrl(fileInfo.toUseCase())
}
