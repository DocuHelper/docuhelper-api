package org.bmserver.gateway.file.request

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bmserver.core.file.useCase.CreateUploadUrlUseCase
import org.bmserver.gateway.common.AbstractAuthRequest

class CreateUploadUrlRequest(
    val name: String,
    val extension: String,
    val size: Long,
) : AbstractAuthRequest() {
    @GraphQLIgnore
    fun toUseCase() =
        CreateUploadUrlUseCase(
            name,
            extension,
            size,
        )
}
