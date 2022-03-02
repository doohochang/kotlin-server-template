import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

/** Subprojects from here. */
val DependencyHandlerScope.DOMAIN get() = project(":subproject:domain")
val DependencyHandlerScope.INFRASTRUCTURE get() = project(":subproject:infrastructure")
val DependencyHandlerScope.PRESENTATION get() = project(":subproject:presentation")
val DependencyHandlerScope.BOOT get() = project(":subproject:boot")

/** External libraries from here. */
val DependencyHandlerScope.KOTLINX_COROUTINES_CORE
    get() = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.KOTLINX_COROUTINES}"

val DependencyHandlerScope.KOTLINX_SERIALIZATION_JSON
    get() = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.KOTLINX_SERIALIZATION_JSON}"

val DependencyHandlerScope.ARROW_CORE get() = "io.arrow-kt:arrow-core:${Version.ARROW}"

val DependencyHandlerScope.LOGBACK get() = "ch.qos.logback:logback-classic:${Version.LOGBACK}"

val DependencyHandlerScope.KTOR_SERVER_CORE get() = "io.ktor:ktor-server-core:${Version.KTOR}"
val DependencyHandlerScope.KTOR_SERVER_NETTY get() = "io.ktor:ktor-server-netty:${Version.KTOR}"
val DependencyHandlerScope.KTOR_SERIALIZATION get() = "io.ktor:ktor-serialization:${Version.KTOR}"

val DependencyHandlerScope.TYPESAFE_CONFIG get() = "com.typesafe:config:${Version.TYPESAFE_CONFIG}"
