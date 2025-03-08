package org.bmserver.gateway.file.request

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bmserver.core.file.useCase.CreateUploadUrlUseCase

class CreateUploadUrlRequest(
    val name: String,
    val extension: String,
    val size: Long,
) {
    @GraphQLIgnore
    fun toUseCase() =
        CreateUploadUrlUseCase(
            name,
            extension,
            size,
        )
}
