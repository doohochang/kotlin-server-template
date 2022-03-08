package io.github.doohochang.ktserver.service

import arrow.core.Either
import io.github.doohochang.ktserver.entity.User
import io.github.doohochang.ktserver.repository.UserRepository
import io.github.doohochang.ktserver.util.randomAlphanumericString
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class UserServiceSpec : FreeSpec({
    val userRepository = mockk<UserRepository>()
    val userService = UserService(userRepository)

    fun randomUser(): User =
        User(
            id = randomAlphanumericString(32),
            name = randomAlphanumericString(32)
        )

    afterTest {
        clearMocks(userRepository)
    }

    "get" - {
        val user = randomUser()

        "should return a user when the repository returns it." {
            coEvery { userRepository.find(user.id) } returns Either.Right(user)
            userService.get(user.id) shouldBe Either.Right(user)
            coVerify(exactly = 1) { userRepository.find(user.id) }
        }

        "should return null when the repository returns null." {
            coEvery { userRepository.find(user.id) } returns Either.Right(null)
            userService.get(user.id) shouldBe Either.Right(null)
            coVerify(exactly = 1) { userRepository.find(user.id) }
        }

        "should return Either.Left when the repository fails." {
            coEvery { userRepository.find(user.id) } returns Either.Left(UserRepository.Dto.FindFailure("cause"))
            userService.get(user.id) shouldBe Either.Left(UserService.Dto.GetFailure("cause"))
            coVerify(exactly = 1) { userRepository.find(user.id) }
        }
    }

    "create" - {
        val user = randomUser()

        "should return a user when the repository succeeds and the name is valid." {
            coEvery { userRepository.create(user.name) } returns Either.Right(user)
            userService.create(user.name) shouldBe Either.Right(user)
            coVerify(exactly = 1) { userRepository.create(user.name) }
        }

        "should return a user when the name is invalid." {
            userService.create("!@#$%")
                .shouldBeLeft()
                .shouldBeTypeOf<UserService.Dto.CreateFailure.InvalidName>()
            coVerify(exactly = 0) { userRepository.create(any()) }
        }

        "should return Either.Left when the repository fails." {
            coEvery { userRepository.create(user.name) } returns Either.Left(UserRepository.Dto.CreateFailure("cause"))
            userService.create(user.name) shouldBe Either.Left(UserService.Dto.CreateFailure.TransactionFailed("cause"))
            coVerify(exactly = 1) { userRepository.create(user.name) }
        }
    }

    "update" - {
        val user = randomUser()
        val newUserName = randomAlphanumericString(32)
        val updatedUser = User(user.id, newUserName)

        "should return the updated user when the repository succeeds." {
            coEvery { userRepository.update(user.id, newUserName) } returns Either.Right(updatedUser)
            userService.update(user.id, newUserName) shouldBe Either.Right(updatedUser)
            coVerify(exactly = 1) { userRepository.update(user.id, newUserName) }
        }

        "should fail when the given user name is invalid." {
            coEvery { userRepository.update(user.id, "!@#$%") } returns Either.Right(User(user.id, "!@#$%"))
            userService.update(user.id, "!@#$%")
                .shouldBeLeft()
                .shouldBeTypeOf<UserService.Dto.UpdateFailure.InvalidName>()
            coVerify(exactly = 0) { userRepository.update(user.id, "!@#$%") }
        }

        "should fail when no user with the given id exists." {
            coEvery { userRepository.update(user.id, newUserName) } returns Either.Left(
                UserRepository.Dto.UpdateFailure.UserDoesNotExist(user.id)
            )
            userService.update(user.id, newUserName) shouldBe Either.Left(
                UserService.Dto.UpdateFailure.UserDoesNotExist(user.id)
            )
            coVerify(exactly = 1) { userRepository.update(user.id, newUserName) }
        }

        "should fail when the update transaction fails." {
            coEvery { userRepository.update(user.id, newUserName) } returns Either.Left(
                UserRepository.Dto.UpdateFailure.TransactionFailed("cause")
            )
            userService.update(user.id, newUserName) shouldBe Either.Left(
                UserService.Dto.UpdateFailure.TransactionFailed("cause")
            )
            coVerify(exactly = 1) { userRepository.update(user.id, newUserName) }
        }
    }

    "delete" - {
        val user = randomUser()

        "should succeed when the repository succeeds." {
            coEvery { userRepository.delete(user.id) } returns Either.Right(Unit)
            userService.delete(user.id) shouldBe Either.Right(Unit)
            coVerify(exactly = 1) { userRepository.delete(user.id) }
        }

        "should fail when no user with the given id exists." {
            coEvery { userRepository.delete(user.id) } returns Either.Left(
                UserRepository.Dto.DeleteFailure.UserDoesNotExist(user.id)
            )
            userService.delete(user.id) shouldBe Either.Left(UserService.Dto.DeleteFailure.UserDoesNotExist(user.id))
            coVerify(exactly = 1) { userRepository.delete(user.id) }
        }

        "should fail when the delete transaction fails." {
            coEvery { userRepository.delete(user.id) } returns Either.Left(UserRepository.Dto.DeleteFailure.TransactionFailed("cause"))
            userService.delete(user.id) shouldBe Either.Left(UserService.Dto.DeleteFailure.TransactionFailed("cause"))
            coVerify(exactly = 1) { userRepository.delete(user.id) }
        }
    }
})
