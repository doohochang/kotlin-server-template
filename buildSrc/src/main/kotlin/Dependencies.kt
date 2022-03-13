import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

/** Subprojects from here. */
val DependencyHandlerScope.DOMAIN get() = project(":subproject:domain")
val DependencyHandlerScope.INFRASTRUCTURE get() = project(":subproject:infrastructure")
val DependencyHandlerScope.PRESENTATION get() = project(":subproject:presentation")
val DependencyHandlerScope.BOOT get() = project(":subproject:boot")
val DependencyHandlerScope.LOGGING get() = project(":subproject:logging")

/** External libraries from here. */
val DependencyHandlerScope.KOTEST get() = "io.kotest:kotest-runner-junit5:${Version.KOTEST}"
val DependencyHandlerScope.KOTEST_ASSERTIONS_ARROW
    get() = "io.kotest.extensions:kotest-assertions-arrow:${Version.KOTEST_ASSERTIONS_ARROW}"

val DependencyHandlerScope.TESTCONTAINERS_BOM
    get() = platform("org.testcontainers:testcontainers-bom:${Version.TESTCONTAINERS_BOM}")
val DependencyHandlerScope.TESTCONTAINERS get() = "org.testcontainers:testcontainers"
val DependencyHandlerScope.TESTCONTAINERS_POSTGRESQL get() = "org.testcontainers:postgresql"

val DependencyHandlerScope.MOCK_K get() = "io.mockk:mockk:${Version.MOCK_K}"

val DependencyHandlerScope.KOTLINX_COROUTINES_CORE
    get() = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.KOTLINX_COROUTINES}"
val DependencyHandlerScope.KOTLINX_COROUTINES_REACTIVE get() = "org.jetbrains.kotlinx:kotlinx-coroutines-reactive"
val DependencyHandlerScope.KOTLINX_COROUTINES_REACTOR get() = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor"

val DependencyHandlerScope.KOTLINX_SERIALIZATION_JSON
    get() = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.KOTLINX_SERIALIZATION_JSON}"

val DependencyHandlerScope.ARROW_CORE get() = "io.arrow-kt:arrow-core:${Version.ARROW}"

val DependencyHandlerScope.LOGBACK get() = "ch.qos.logback:logback-classic:${Version.LOGBACK}"

val DependencyHandlerScope.KTOR_SERVER_CORE get() = "io.ktor:ktor-server-core:${Version.KTOR}"
val DependencyHandlerScope.KTOR_SERVER_NETTY get() = "io.ktor:ktor-server-netty:${Version.KTOR}"
val DependencyHandlerScope.KTOR_SERIALIZATION get() = "io.ktor:ktor-serialization:${Version.KTOR}"
val DependencyHandlerScope.KTOR_CLIENT_CORE get() = "io.ktor:ktor-client-core:${Version.KTOR}"
val DependencyHandlerScope.KTOR_CLIENT_CIO get() = "io.ktor:ktor-client-cio:${Version.KTOR}"

val DependencyHandlerScope.TYPESAFE_CONFIG get() = "com.typesafe:config:${Version.TYPESAFE_CONFIG}"

val DependencyHandlerScope.SPRING_DATA_R2DBC get() = "org.springframework.data:spring-data-r2dbc:${Version.SPRING_DATA_R2DBC}"
val DependencyHandlerScope.R2DBC_BOM get() = platform("io.r2dbc:r2dbc-bom:${Version.R2DBC_BOM}")
val DependencyHandlerScope.R2DBC_POOL get() = "io.r2dbc:r2dbc-pool"
val DependencyHandlerScope.R2DBC_POSTGRESQL get() = "io.r2dbc:r2dbc-postgresql"
