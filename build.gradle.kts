plugins {
    java
    kotlin("jvm") version "1.6.10"
    application
}

/** Settings for all projects from here. */
allprojects {
    group = "io.github.doohochang"
    version = "0.1.0"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib"))

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

        implementation("io.arrow-kt:arrow-core:1.0.1")

        implementation("ch.qos.logback:logback-classic:1.2.10")
    }
}

/** Settings for only the root project from here. */
dependencies {
    implementation(project("subprojects:boot"))
}

application {
    mainClass.set("io.github.doohochang.ktserver.MainKt")
}
