plugins {
    kotlin("jvm") version "1.6.10"
    java

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
    }
}

/** Settings for only the root project from here. */
dependencies {
    implementation(project("subprojects:boot"))
}

application {
    mainClass.set("io.github.doohochang.ktserver.MainKt")
}
