package io.github.doohochang.ktserver.repository

import arrow.core.Either
import io.github.doohochang.ktserver.entity.User

interface UserRepository {
    suspend fun find(id: String): Either<FindFailure, User?>
    suspend fun create(name: String): Either<CreateFailure, User>
    suspend fun update(id: String, name: String): Either<UpdateFailure, Unit>
    suspend fun delete(id: String): Either<DeleteFailure, Unit>

    companion object Dto {
        data class FindFailure(val transactionFailure: String)
        data class CreateFailure(val transactionFailure: String)

        sealed interface UpdateFailure {
            data class UserDoesNotExist(val id: String) : UpdateFailure
            data class TransactionFailed(val cause: String) : UpdateFailure
        }

        sealed interface DeleteFailure {
            data class UserDoesNotExist(val id: String) : DeleteFailure
            data class TransactionFailed(val cause: String) : DeleteFailure
        }
    }
}
