package org.bmserver.core.file.useCase

import org.bmserver.core.file.File

class CreateUploadUrlUseCase(
    val name: String,
    val extension: String,
    val size: Long,
) {
    fun toFile() =
        File(
            name = name,
            size = size,
            extension = extension,
        )
}
