package io.github.doohochang.ktserver

import com.typesafe.config.ConfigFactory
import io.github.doohochang.ktserver.configuration.PostgresqlConfiguration
import io.github.doohochang.ktserver.http.HttpConfiguration
import io.github.doohochang.ktserver.http.HttpServer
import io.github.doohochang.ktserver.repository.PostgresqlConnectionPool
import io.github.doohochang.ktserver.repository.UserRepositoryImpl
import io.github.doohochang.ktserver.service.UserService

data class Application(
    val configuration: Configuration,
    val infrastructure: Infrastructure,
    val domain: Domain,
    val presentation: Presentation
) {
    companion object {
        fun load(): Application {
            val configuration = Configuration.load()
            val infrastructure = Infrastructure.load(configuration)
            val domain = Domain.load(infrastructure)
            val presentation = Presentation.load(configuration, domain)

            return Application(
                configuration,
                infrastructure,
                domain,
                presentation
            )
        }
    }
}

data class Configuration(
    val postgresql: PostgresqlConfiguration,
    val http: HttpConfiguration
) {
    companion object {
        fun load(): Configuration {
            val rootConfiguration = ConfigFactory.load()
            val httpConfiguration = HttpConfiguration.from(rootConfiguration)
            val postgresqlConfiguration = PostgresqlConfiguration.from(rootConfiguration)

            return Configuration(
                postgresqlConfiguration,
                httpConfiguration
            )
        }
    }
}

data class Infrastructure(
    val userRepository: UserRepositoryImpl
) {
    companion object {
        fun load(configuration: Configuration): Infrastructure {
            val postgresqlConnectionPool = PostgresqlConnectionPool(configuration.postgresql)
            val userRepository = UserRepositoryImpl(postgresqlConnectionPool)

            return Infrastructure(userRepository)
        }
    }
}

data class Domain(
    val userService: UserService
) {
    companion object {
        fun load(infrastructure: Infrastructure): Domain {
            val userService = UserService(infrastructure.userRepository)

            return Domain(userService)
        }
    }
}

data class Presentation(
    val httpServer: HttpServer
) {
    companion object {
        fun load(configuration: Configuration, domain: Domain): Presentation {
            val httpServer = HttpServer(configuration.http, domain.userService)

            return Presentation(httpServer)
        }
    }
}
