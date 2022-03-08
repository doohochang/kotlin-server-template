package io.github.doohochang.ktserver.service

import arrow.core.Either
import arrow.core.computations.either
import io.github.doohochang.ktserver.entity.User
import io.github.doohochang.ktserver.repository.UserRepository

class UserService(private val userRepository: UserRepository) {
    suspend fun get(id: String): Either<GetFailure, User?> =
        userRepository.find(id).mapLeft { GetFailure(it.transactionFailure) }

    suspend fun create(name: String): Either<CreateFailure, User> = either {
        User.validateName(name).mapLeft { CreateFailure.InvalidName(name, it) }.bind()
        userRepository.create(name).mapLeft { CreateFailure.TransactionFailed(it.transactionFailure) }.bind()
    }

    suspend fun update(id: String, name: String): Either<UpdateFailure, User> = either {
        User.validateName(name).mapLeft { UpdateFailure.InvalidName(name, it) }.bind()

        userRepository.update(id, name)
            .mapLeft {
                when (it) {
                    is UserRepository.Dto.UpdateFailure.UserDoesNotExist -> UpdateFailure.UserDoesNotExist(it.id)
                    is UserRepository.Dto.UpdateFailure.TransactionFailed -> UpdateFailure.TransactionFailed(it.cause)
                }
            }
            .bind()
    }

    suspend fun delete(id: String): Either<DeleteFailure, Unit> =
        userRepository.delete(id)
            .mapLeft {
                when (it) {
                    is UserRepository.Dto.DeleteFailure.UserDoesNotExist -> DeleteFailure.UserDoesNotExist(it.id)
                    is UserRepository.Dto.DeleteFailure.TransactionFailed -> DeleteFailure.TransactionFailed(it.cause)
                }
            }

    companion object Dto {
        data class GetFailure(val transactionFailure: String)

        sealed interface CreateFailure {
            data class TransactionFailed(val failure: String) : CreateFailure
            data class InvalidName(val name: String, val reason: String) : CreateFailure
        }

        sealed interface UpdateFailure {
            data class TransactionFailed(val failure: String) : UpdateFailure
            data class UserDoesNotExist(val id: String) : UpdateFailure
            data class InvalidName(val name: String, val reason: String) : UpdateFailure
        }

        sealed interface DeleteFailure {
            data class TransactionFailed(val failure: String) : DeleteFailure
            data class UserDoesNotExist(val id: String) : DeleteFailure
        }
    }
}
