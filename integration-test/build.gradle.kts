val ktorVersion = rootProject.properties["ktorVersion"] as String
val komapperVersion = rootProject.properties["komapperVersion"] as String
val domaVersion = rootProject.properties["domaVersion"] as String
val kotestVersion = rootProject.properties["kotestVersion"] as String
val jacksonVersion = rootProject.properties["jacksonVersion"] as String
val logbackVersion = rootProject.properties["logbackVersion"] as String
val slf4jVersion = rootProject.properties["slf4jVersion"] as String


plugins {
    kotlin("jvm")

    id("io.ktor.plugin")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":doma-statistics"))
    implementation(project(":komapper-statistics"))

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-resources:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    // komapper
    implementation("org.komapper:komapper-core:$komapperVersion")
    implementation("org.seasar.doma:doma-core:$domaVersion")


    // log
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:jul-to-slf4j:$slf4jVersion") // for doma

    // test
    testImplementation("io.kotest:kotest-framework-engine-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("kotest.framework.classpath.scanning.config.disable", "true")
}
