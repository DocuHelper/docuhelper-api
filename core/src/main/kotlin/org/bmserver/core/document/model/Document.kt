package org.bmserver.core.document.model

import org.bmserver.core.common.BaseDomain
import java.util.UUID

data class Document(
    val name: String,
    val state: DocumentState,
    val file: UUID,
    val owner: UUID,
) : BaseDomain()
