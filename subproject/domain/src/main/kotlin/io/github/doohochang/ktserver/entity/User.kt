package io.github.doohochang.ktserver.entity

import arrow.core.Either

/**
 * Represents a user whose [name] consists only of alphanumeric characters.
 *
 * @property id the string identifier of the user.
 */
data class User(
    val id: String,
    val name: String
) {
    companion object {
        fun validateName(name: String): Either<String, Unit> =
            if (name.filterNot { it.isDigit() || it in 'a'..'z' || it in 'A'..'Z' }.isNotEmpty())
                Either.Left("A user name should consists of alphanumeric characters.")
            else
                Either.Right(Unit)
    }
}
