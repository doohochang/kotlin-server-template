package io.github.doohochang.ktserver.entity

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec

class UserSpec : FreeSpec({
    "User.validateName should return" - {
        "Either.Left when the name has non-alphanumeric characters." {
            User.validateName("!@ab39").shouldBeLeft()
            User.validateName("한글").shouldBeLeft()
            User.validateName("-_<>").shouldBeLeft()
            User.validateName(" \n").shouldBeLeft()
        }

        "Either.Right when the name consists only of alphanumeric characters." {
            User.validateName("12345").shouldBeRight()
            User.validateName("abcde").shouldBeRight()
            User.validateName("XYZ").shouldBeRight()
            User.validateName("abc123").shouldBeRight()
            User.validateName("1q2w3e4r5t").shouldBeRight()
        }
    }
})
