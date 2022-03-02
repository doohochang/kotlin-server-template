plugins {
    java
    kotlin("jvm") version Version.KOTLIN
    application
}

/** Settings for all projects from here. */
allprojects {
    group = "io.github.doohochang"
    version = Version.KOTLIN_SERVER_TEMPLATE

    applyKotlinJvmPlugin()
    applyJavaPlugin()

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib"))

        implementation(KOTLINX_COROUTINES_CORE)
        implementation(ARROW_CORE)
        implementation(LOGBACK)
    }
}

/** Settings for only the root project from here. */
dependencies {
    implementation(BOOT)
}

application {
    mainClass.set("io.github.doohochang.ktserver.MainKt")
}
