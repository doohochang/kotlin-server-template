plugins {
    java
    kotlinJvm
    application

    gradleKtlint
}

/** Settings for all projects from here. */
allprojects {
    group = "io.github.doohochang"
    version = Version.KOTLIN_SERVER_TEMPLATE

    applyJavaPlugin()
    applyKotlinJvmPlugin()
    applyGradleKtlintPlugin()

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib"))

        implementation(KOTLINX_COROUTINES_CORE)
        implementation(ARROW_CORE)
        implementation(LOGBACK)
    }

    /** Lint settings. */
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        disabledRules.set(setOf("no-wildcard-imports"))
    }
}

/** Settings for only the root project from here. */
dependencies {
    implementation(BOOT)
}

application {
    mainClass.set("io.github.doohochang.ktserver.MainKt")
}
