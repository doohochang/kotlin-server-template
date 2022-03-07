package io.github.doohochang.ktserver.repository

import arrow.core.Either
import io.github.doohochang.ktserver.entity.User
import io.github.doohochang.ktserver.util.randomAlphanumericString
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.*
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.data.relational.core.query.Update

class UserRepositoryImpl(private val connectionFactory: PostgresqlConnectionFactory) : UserRepository {
    private val template = R2dbcEntityTemplate(connectionFactory)

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

    override suspend fun update(id: String, name: String): Either<UserRepository.Dto.UpdateFailure, Unit> = try {
        val updatedCount = template
            .update<UserRow>()
            .matching(
                query(where(USER_ID).`is`(id))
            )
            .apply(Update.update(USER_NAME, name))
            .awaitSingle()

        if (updatedCount == 0) Either.Left(UserRepository.Dto.UpdateFailure.UserDoesNotExist(id))
        else Either.Right(Unit)
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
