package io.github.doohochang.ktserver.util

fun randomAlphanumericString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}
