repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":subproject:domain"))
    implementation(project(":subproject:infrastructure"))
    implementation(project(":subproject:presentation"))
}
