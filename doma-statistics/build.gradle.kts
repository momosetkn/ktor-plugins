val ktorVersion = rootProject.properties["ktorVersion"] as String
val domaVersion = rootProject.properties["domaVersion"] as String
val kotestVersion = rootProject.properties["kotestVersion"] as String

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    // type safe routing
    implementation("io.ktor:ktor-server-resources:$ktorVersion")

    // doma2
    implementation("org.seasar.doma:doma-core:$domaVersion")
    implementation("org.seasar.doma:doma-kotlin:$domaVersion")

    // test
    testImplementation("io.kotest:kotest-framework-engine-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("kotest.framework.classpath.scanning.config.disable", "true")
}
