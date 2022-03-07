package io.github.doohochang.ktserver.configuration

import arrow.core.Either
import com.typesafe.config.Config

data class PostgresqlConfiguration(
    val host: String,
    val port: Int,
    val database: String,
    val username: String,
    val password: String
) {
    companion object {
        fun from(config: Config): Either<Throwable, PostgresqlConfiguration> =
            try {
                val postgresqlConfig = config.getConfig("postgresql")
                Either.Right(
                    PostgresqlConfiguration(
                        host = postgresqlConfig.getString("host"),
                        port = postgresqlConfig.getInt("port"),
                        database = postgresqlConfig.getString("database"),
                        username = postgresqlConfig.getString("username"),
                        password = postgresqlConfig.getString("password")
                    )
                )
            } catch (failure: Throwable) {
                Either.Left(failure)
            }
    }
}
