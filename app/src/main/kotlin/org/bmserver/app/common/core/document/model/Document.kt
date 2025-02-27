package org.bmserver.app.common.core.document.model

import org.bmserver.app.common.core.common.BaseDomain
import java.util.UUID

data class Document(
    val name: String,
    val state: DocumentState,
    val file: UUID,
    val owner: UUID,
) : BaseDomain()
