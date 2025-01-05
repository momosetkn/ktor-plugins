val ktorVersion = rootProject.properties["ktorVersion"] as String
val komapperVersion = rootProject.properties["komapperVersion"] as String
val kotestVersion = rootProject.properties["kotestVersion"] as String

dependencies {
    implementation(project(":doma-statistics"))
    implementation(project(":komapper-statistics"))

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    implementation("io.ktor:ktor-server-resources:$ktorVersion")

    // komapper
    implementation("org.komapper:komapper-core:$komapperVersion")

    // test
    testImplementation("io.kotest:kotest-framework-engine-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("kotest.framework.classpath.scanning.config.disable", "true")
}
