package org.bmserver.core.file.response

import java.net.URL
import java.util.UUID

data class UploadUrl(
    val uuid: UUID,
    val url: URL,
)
