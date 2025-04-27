package org.bmserver.core.ai

data class ChatResult(
    val message: String,
    val token: Token? = null
)

data class Token(
    val promotToken: Int,
    val messageToken: Int,
    val totalToken: Int
)