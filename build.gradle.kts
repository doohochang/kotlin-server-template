plugins {
    java
    kotlinJvm
    application

    ktlint
    shadow
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
        implementation(kotlin("reflect"))

        implementation(KOTLINX_COROUTINES_CORE)
        implementation(ARROW_CORE)

        testImplementation(KOTEST)
        testImplementation(KOTEST_ASSERTIONS_ARROW)
        testImplementation(MOCK_K)
    }

    /** Lint settings. */
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        disabledRules.set(setOf("no-wildcard-imports"))
    }

    /** Test settings. */
    tasks.withType<Test>().configureEach {
        useJUnitPlatform() // Platform setting for Kotest.
        testLogging.showStandardStreams = true // Prints log while test.
    }
}

/** Settings for only the root project from here. */
dependencies {
    implementation(BOOT)
}

application {
    mainClass.set("io.github.doohochang.ktserver.MainKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "io.github.doohochang.ktserver.MainKt"))
        }
    }
}
