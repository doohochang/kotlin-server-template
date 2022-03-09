package io.github.doohochang.ktserver.repository

import arrow.core.Either
import io.github.doohochang.ktserver.entity.User
import io.github.doohochang.ktserver.util.randomAlphanumericString
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.*
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

open class UserRepositoryImpl(connectionFactory: ConnectionFactory) : UserRepository {
    private val template = R2dbcEntityTemplate(connectionFactory)
    private val databaseClient = template.databaseClient
    private val converter = template.converter

    override suspend fun find(id: String): Either<UserRepository.Dto.FindFailure, User?> = try {
        val row = template
            .select<UserRow>()
            .from(USER_TABLE)
            .matching(
                query(where(USER_ID).`is`(id))
            )
            .awaitOneOrNull()

        Either.Right(row?.toDomain())
    } catch (e: Throwable) {
        Either.Left(UserRepository.Dto.FindFailure(e.stackTraceToString()))
    }

    override suspend fun create(name: String): Either<UserRepository.Dto.CreateFailure, User> = try {
        val row = template
            .insert(
                UserRow(
                    id = randomAlphanumericString(32),
                    name = name
                )
            )
            .awaitSingle()

        Either.Right(row.toDomain())
    } catch (e: Throwable) {
        Either.Left(UserRepository.Dto.CreateFailure(e.stackTraceToString()))
    }

    @Transactional(isolation = Isolation.READ_COMMITTED) // An example using @Transactional.
    override suspend fun update(id: String, name: String): Either<UserRepository.Dto.UpdateFailure, User> = try {
        // Another querying style using DatabaseClient instead of R2dbcEntityTemplate.
        val updatedUser = databaseClient
            .sql(
                """
                update $USER_TABLE
                set name = :name
                where id = :id
                returning *
                """.trimIndent()
            )
            .bind("name", name)
            .bind("id", id)
            .convert<UserRow>(converter)
            .awaitSingleOrNull()

        if (updatedUser == null) Either.Left(UserRepository.Dto.UpdateFailure.UserDoesNotExist(id))
        else Either.Right(updatedUser.toDomain())
    } catch (e: Throwable) {
        Either.Left(UserRepository.Dto.UpdateFailure.TransactionFailed(e.stackTraceToString()))
    }

    override suspend fun delete(id: String): Either<UserRepository.Dto.DeleteFailure, Unit> = try {
        val deletedCount = template
            .delete<UserRow>()
            .matching(
                query(where(USER_ID).`is`(id))
            )
            .all()
            .awaitSingle()

        if (deletedCount == 0) Either.Left(UserRepository.Dto.DeleteFailure.UserDoesNotExist(id))
        else Either.Right(Unit)
    } catch (e: Throwable) {
        Either.Left(UserRepository.Dto.DeleteFailure.TransactionFailed(e.stackTraceToString()))
    }

    companion object {
        const val USER_TABLE = "\"user\""
        const val USER_ID = "id"
        const val USER_NAME = "name"

        @Table(USER_TABLE)
        data class UserRow(
            @Column(USER_ID) val id: String,
            @Column(USER_NAME) val name: String
        )

        private fun UserRow.toDomain(): User = User(id, name)
    }
}
