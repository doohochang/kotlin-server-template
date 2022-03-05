plugins {
    java
    kotlinJvm
    application

    gradleKtlint
    gradleShadow
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

        testImplementation(KOTEST)
        testImplementation(KOTEST_ASSERTIONS_ARROW)
    }

    /** Lint settings. */
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        disabledRules.set(setOf("no-wildcard-imports"))
    }

    /** Test platform setting for Kotest. */
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
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
