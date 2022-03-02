import org.gradle.api.plugins.PluginAware
import org.gradle.kotlin.dsl.apply

fun PluginAware.applyKotlinJvmPlugin() {
    apply(plugin = "org.jetbrains.kotlin.jvm")
}

fun PluginAware.applyJavaPlugin() {
    apply(plugin = "java")
}
