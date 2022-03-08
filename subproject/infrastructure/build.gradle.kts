dependencies {
    implementation(DOMAIN)
    implementation(LOGGING)

    implementation(TYPESAFE_CONFIG)

    implementation(SPRING_DATA_R2DBC)
    implementation(R2DBC_BOM)
    implementation(R2DBC_POOL)
    implementation(R2DBC_POSTGRESQL)

    implementation(KOTLINX_COROUTINES_REACTIVE)
    implementation(KOTLINX_COROUTINES_REACTOR)
}
