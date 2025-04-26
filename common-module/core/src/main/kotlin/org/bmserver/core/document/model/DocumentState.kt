package org.bmserver.core.document.model

enum class DocumentState {
    READING,
    PARSING,
    EMBEDDING,
    COMPLETE,
    Fail
}
