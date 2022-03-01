plugins {
    kotlin("jvm") version "1.6.10"
    java

    application
}

group = "io.github.doohochang"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

application {
    mainClass.set("io.github.doohochang.ktserver.MainKt")
}
