package io.github.doohochang.ktserver.repository

import io.github.doohochang.ktserver.configuration.PostgresqlConfiguration
import io.r2dbc.pool.PoolingConnectionFactoryProvider
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions

class PostgresqlConnectionPool(
    configuration: PostgresqlConfiguration
) {
    internal val instance = ConnectionFactories.get(
        ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.DRIVER, "pool")
            .option(ConnectionFactoryOptions.PROTOCOL, "postgresql")
            .option(ConnectionFactoryOptions.HOST, configuration.host)
            .option(ConnectionFactoryOptions.PORT, configuration.port)
            .option(ConnectionFactoryOptions.USER, configuration.username)
            .option(ConnectionFactoryOptions.PASSWORD, configuration.password)
            .option(ConnectionFactoryOptions.DATABASE, configuration.database)
            .option(PoolingConnectionFactoryProvider.INITIAL_SIZE, configuration.connectionInitialCount)
            .option(PoolingConnectionFactoryProvider.MAX_SIZE, configuration.connectionMaxCount)
            .build()
    )
}
