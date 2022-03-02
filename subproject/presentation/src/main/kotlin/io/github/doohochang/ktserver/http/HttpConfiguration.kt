package io.github.doohochang.ktserver.http

import arrow.core.Either
import com.typesafe.config.Config

data class HttpConfiguration(
    val port: Int
) {
    companion object {
        fun from(config: Config): Either<Throwable, HttpConfiguration> =
            try {
                Either.Right(
                    HttpConfiguration(
                        port = config.getInt("http.port")
                    )
                )
            } catch (failure: Throwable) {
                Either.Left(failure)
            }
    }
}
