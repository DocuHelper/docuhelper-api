package org.bmserver.core.ai

data class ChatResult<T>(
    val message: T,
    val token: Token? = null
)

data class Token(
    val promotToken: Int,
    val messageToken: Int,
    val totalToken: Int
)