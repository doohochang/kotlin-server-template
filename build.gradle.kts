plugins {
    kotlin("jvm") version Version.KOTLIN
    java

    application
}

/** Settings for all projects from here. */
allprojects {
    group = "io.github.doohochang"
    version = Version.KT_SERVER

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
