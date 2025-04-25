package org.bmserver.core.document.model

import org.bmserver.core.common.domain.BaseDomain
import java.util.UUID

data class Document(
    val name: String,
    val type: DocumentType,
    var state: DocumentState = DocumentState.READING,
    val file: UUID,
    val owner: UUID,
) : BaseDomain() {

    fun updateDocumentState(state: DocumentState): Document {
        this.state = state
        return this
    }
}
