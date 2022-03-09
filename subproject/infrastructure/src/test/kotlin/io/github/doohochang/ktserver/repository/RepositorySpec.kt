package io.github.doohochang.ktserver.repository

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions.*
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.await
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer

class RepositorySpec : FreeSpec({
    val log = LoggerFactory.getLogger(this::class.java)
    val container = PostgreSQLContainer(POSTGRESQL_IMAGE_TAG)
        .withExposedPorts(POSTGRESQL_PORT)
        .withDatabaseName(POSTGRESQL_DATABASE)
        .withUsername(POSTGRESQL_USERNAME)
        .withPassword(POSTGRESQL_PASSWORD)
        .withLogConsumer(Slf4jLogConsumer(log))

    val connectionPool by lazy {
        ConnectionFactories.get(
            builder()
                .option(DRIVER, "pool")
                .option(PROTOCOL, "postgresql")
                .option(HOST, "localhost")
                .option(PORT, container.getMappedPort(POSTGRESQL_PORT))
                .option(USER, POSTGRESQL_USERNAME)
                .option(PASSWORD, POSTGRESQL_PASSWORD)
                .option(DATABASE, POSTGRESQL_DATABASE)
                .option(MAX_SIZE, CONNECTION_POOL_MAX_SIZE)
                .build()
        )
    }

    beforeSpec {
        container.start()

        val databaseClient = DatabaseClient.create(connectionPool)
        val initScript = this::class.java.classLoader.getResource(POSTGRESQL_INIT_SCRIPT_PATH)!!.readText()
        databaseClient.sql(initScript).await()
    }

    afterSpec {
        container.stop()
    }

    "Integration test for repositories" - {

        "UserRepositoryImpl" {
            val repository = UserRepositoryImpl(connectionPool)

            // Tests create from here.
            val user1 = repository.create("TestUserName1").shouldBeRight()
            val user2 = repository.create("TestUserName2").shouldBeRight()
            repository.find(user1.id) shouldBeRight user1

            // Tests find from here.
            val nonExistingId = "NonExistingId"
            repository.find(user2.id) shouldBeRight user2
            repository.find(nonExistingId) shouldBeRight null

            // Tests update from here.
            val nameToUpdate = "UpdatedUserName"
            val updatedUser = repository.update(user2.id, nameToUpdate).shouldBeRight()
            updatedUser.id shouldBe user2.id
            updatedUser.name shouldBe nameToUpdate
            repository.find(user2.id).shouldBeRight(updatedUser)

            repository.update(nonExistingId, "NewName")
                .shouldBeLeft(UserRepository.Dto.UpdateFailure.UserDoesNotExist(nonExistingId))
            repository.find(nonExistingId) shouldBeRight null

            // Tests delete from here.
            repository.delete(user1.id).shouldBeRight(Unit)
            repository.delete(nonExistingId)
                .shouldBeLeft(UserRepository.Dto.DeleteFailure.UserDoesNotExist(nonExistingId))
            repository.delete(user2.id).shouldBeRight(Unit)
        }
    }
}) {
    companion object {
        const val POSTGRESQL_IMAGE_TAG = "postgres:14.2"
        const val POSTGRESQL_DATABASE = "testdb"
        const val POSTGRESQL_USERNAME = "tester"
        const val POSTGRESQL_PASSWORD = "tester"
        const val POSTGRESQL_PORT = 5432
        const val POSTGRESQL_INIT_SCRIPT_PATH = "init-test.sql"
        const val CONNECTION_POOL_MAX_SIZE = 20
    }
}
