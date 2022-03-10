package io.github.doohochang.ktserver.json

import kotlinx.serialization.Serializable

@Serializable
data class PatchUserRequest(val name: String)
