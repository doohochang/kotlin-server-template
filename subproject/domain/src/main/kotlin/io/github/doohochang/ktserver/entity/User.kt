package io.github.doohochang.ktserver.entity

/**
 * Represents a user whose [name] consists only of alphanumeric characters.
 *
 * @property id the string identifier of the user.
 */
data class User(
    val id: String,
    val name: String
)
