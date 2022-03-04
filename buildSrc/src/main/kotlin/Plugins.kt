import org.gradle.api.plugins.PluginAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec

fun PluginAware.applyJavaPlugin() {
    apply(plugin = "java")
}

val PluginDependenciesSpec.kotlinJvm get() = kotlin("jvm") version Version.KOTLIN
fun PluginAware.applyKotlinJvmPlugin() {
    apply(plugin = "org.jetbrains.kotlin.jvm")
}

val PluginDependenciesSpec.gradleKtlint get() = id("org.jlleitschuh.gradle.ktlint") version Version.GRADLE_KTLINT
fun PluginAware.applyGradleKtlintPlugin() {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

val PluginDependenciesSpec.gradleShadow get() = id("com.github.johnrengelman.shadow") version Version.GRADLE_SHADOW
