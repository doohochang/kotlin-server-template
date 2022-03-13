package io.github.doohochang.ktserver.configuration

import com.typesafe.config.Config

data class PostgresqlConfiguration(
    val host: String,
    val port: Int,
    val database: String,
    val username: String,
    val password: String,
    val connectionInitialCount: Int,
    val connectionMaxCount: Int
) {
    companion object {
        fun from(config: Config): PostgresqlConfiguration {
            val postgresqlConfig = config.getConfig("postgresql")

            return PostgresqlConfiguration(
                host = postgresqlConfig.getString("host"),
                port = postgresqlConfig.getInt("port"),
                database = postgresqlConfig.getString("database"),
                username = postgresqlConfig.getString("username"),
                password = postgresqlConfig.getString("password"),
                connectionInitialCount = postgresqlConfig.getInt("connection-initial-count"),
                connectionMaxCount = postgresqlConfig.getInt("connection-max-count")
            )
        }
    }
}
