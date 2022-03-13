package io.github.doohochang.ktserver.json

import kotlinx.serialization.Serializable

@Serializable
data class PostUserRequest(val name: String)
