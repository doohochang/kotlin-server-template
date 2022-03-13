package io.github.doohochang.ktserver

import io.github.doohochang.ktserver.repository.Postgresql
import io.kotest.core.spec.style.FreeSpec

class IntegrationTestSpec : FreeSpec({
    "IntegrationTestSpec" - {
        val originalConfiguration = Configuration.load()

        val postgresql = Postgresql(
            database = originalConfiguration.postgresql.database,
            username = originalConfiguration.postgresql.username,
            password = originalConfiguration.postgresql.password
        )

        val postgresqlPort = postgresql.startAndInitialize()

        val configuration = originalConfiguration.copy(
            postgresql = originalConfiguration.postgresql.copy(port = postgresqlPort)
        )

        val infrastructure = Infrastructure.load(configuration)
        val domain = Domain.load(infrastructure)
        val presentation = Presentation.load(configuration, domain)

        testHttpServer(presentation.httpServer, configuration.http)

        postgresql.stop()
    }
})
