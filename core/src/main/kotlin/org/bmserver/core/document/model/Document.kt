package org.bmserver.core.document.model

import org.bmserver.core.common.domain.BaseDomain
import java.util.UUID

data class Document(
    val name: String,
    var state: DocumentState = DocumentState.READING,
    val file: UUID,
    val owner: UUID,
) : BaseDomain() {
    constructor(name: String, file: UUID, owner: UUID) : this(
        name = name,
        state = DocumentState.READING,
        file = file,
        owner = owner,
    )

    fun updateDocumentState(state: DocumentState): Document {
        this.state = state
        return this
    }
}
