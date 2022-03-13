package io.github.doohochang.ktserver.http

import com.typesafe.config.Config

data class HttpConfiguration(
    val port: Int
) {
    companion object {
        fun from(config: Config): HttpConfiguration =
            HttpConfiguration(
                port = config.getInt("http.port")
            )
    }
}
