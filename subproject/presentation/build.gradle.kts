plugins {
    kotlin("plugin.serialization") version Version.KOTLIN
}

dependencies {
    api(DOMAIN)

    implementation(KOTLINX_SERIALIZATION_JSON)

    implementation(KTOR_SERVER_CORE)
    implementation(KTOR_SERVER_NETTY)
    implementation(KTOR_SERIALIZATION)

    implementation(TYPESAFE_CONFIG)
}
