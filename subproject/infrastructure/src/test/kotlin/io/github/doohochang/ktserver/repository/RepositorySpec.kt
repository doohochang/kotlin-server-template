package io.github.doohochang.ktserver.repository

import io.github.doohochang.ktserver.configuration.PostgresqlConfiguration
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

/**
 * Contains integration tests for repositories in infrastructure layer.
 * It tests the repositories with database containers instantiated by Testcontainers.
 */
class RepositorySpec : FreeSpec({
    "Integration test for repositories" - {
        val postgresql = Postgresql(POSTGRESQL_DATABASE, POSTGRESQL_USERNAME, POSTGRESQL_PASSWORD)

        val postgresqlPort = postgresql.startAndInitialize()

        val connectionPool = PostgresqlConnectionPool(
            PostgresqlConfiguration(
                host = POSTGRESQL_HOST,
                port = postgresqlPort,
                database = POSTGRESQL_DATABASE,
                username = POSTGRESQL_USERNAME,
                password = POSTGRESQL_PASSWORD,
                connectionInitialCount = CONNECTION_POOL_INITIAL_COUNT,
                connectionMaxCount = CONNECTION_POOL_MAX_COUNT
            )
        )

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

        postgresql.stop()
    }
}) {
    companion object {
        const val POSTGRESQL_DATABASE = "testdb"
        const val POSTGRESQL_USERNAME = "tester"
        const val POSTGRESQL_PASSWORD = "tester"
        const val POSTGRESQL_HOST = "localhost"
        const val CONNECTION_POOL_INITIAL_COUNT = 10
        const val CONNECTION_POOL_MAX_COUNT = 20
    }
}
