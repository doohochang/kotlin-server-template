package io.github.doohochang.ktserver.json

import kotlinx.serialization.Serializable
import io.github.doohochang.ktserver.entity.User as DomainUser

@Serializable
data class User(
    val id: String,
    val name: String
)

fun User.toDomain(): DomainUser = DomainUser(id, name)
fun DomainUser.toJson(): User = User(id, name)
