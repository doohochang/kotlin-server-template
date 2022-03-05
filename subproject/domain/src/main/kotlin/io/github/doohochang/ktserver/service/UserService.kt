package io.github.doohochang.ktserver.service

import arrow.core.Either
import io.github.doohochang.ktserver.entity.User
import io.github.doohochang.ktserver.repository.UserRepository

interface UserService {
    suspend fun get(id: String): Either<GetFailure, User?>
    suspend fun create(name: String): Either<CreateFailure, User>
    suspend fun update(id: String, name: String): Either<UpdateFailure, User>
    suspend fun delete(id: String): Either<DeleteFailure, User>

    companion object Dto {
        data class GetFailure(val repositoryFailure: UserRepository.Dto.FindFailure)

        sealed interface CreateFailure {
            data class RepositoryFailed(val failure: UserRepository.Dto.CreateFailure) : CreateFailure
            data class InvalidName(val name: String) : CreateFailure
        }

        sealed interface UpdateFailure {
            data class RepositoryFailed(val failure: UserRepository.Dto.CreateFailure) : UpdateFailure
            data class UserDoesNotExist(val id: String) : UpdateFailure
            data class InvalidName(val name: String) : UpdateFailure
        }

        sealed interface DeleteFailure {
            data class RepositoryFailed(val failure: UserRepository.Dto.CreateFailure) : UpdateFailure
            data class UserDoesNotExist(val id: String) : UpdateFailure
        }
    }
}
