val kotestVersion: String by project
val kotlinVersion: String by project
val artifactIdPrefix: String by project
val artifactVersion: String by project
val artifactGroup: String by project

plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    `maven-publish`
    id("io.deepmedia.tools.deployer") version "0.15.0"
    id("org.jetbrains.dokka") version "1.9.20"
}

group = artifactGroup
version = artifactVersion
description = "Ktor plugins"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
        mavenLocal()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.addAll(listOf("-Xjvm-default=all"))
        }
    }

    detekt {
        parallel = true
        autoCorrect = true
        config.from("$rootDir/config/detekt/detekt.yml")
        buildUponDefaultConfig = true
        basePath = rootDir.absolutePath
    }

    ktlint {
        version.set("1.5.0")
        filter {
            exclude { entry ->
                entry.file.toString().contains("/generated/")
            }
        }
    }
}

val libraryProjects =
    subprojects.filter {
        // To prevent accidental publishing, use a allowlist.
        it.name in listOf(
            "doma-statistics",
            "komapper-statistics",
        )
    }
configure(libraryProjects) {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "io.deepmedia.tools.deployer")
    apply(plugin = "org.jetbrains.dokka")

    version = rootProject.version

    val sourcesJar by tasks.creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
    val javadocs = tasks.register<Jar>("dokkaJavadocJar") {
        dependsOn(tasks.dokkaJavadoc)
        from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
        archiveClassifier.set("javadoc")
    }

    deployer {
        val projectUrl: String by project
        val artifactVersion: String by project
        val autherName: String by project
        val autherEmail: String by project

        projectInfo {
            name = rootProject.name
            description = rootProject.description
            url = projectUrl
            groupId = artifactGroup
            artifactId = "$artifactIdPrefix-${project.name}"
            scm {
                fromGithub(autherName, artifactIdPrefix)
            }
            license(apache2)
            developer(autherName, autherEmail)
        }
        content {
            component {
                sources(sourcesJar)
                docs(javadocs)
                fromJava()
            }
        }
        release {
            release.version = artifactVersion
            release.tag = artifactVersion
            release.description = project.description
        }
        centralPortalSpec {
            auth.user = secret("CENTRAL_PORTAL_USER")
            auth.password = secret("CENTRAL_PORTAL_PASSWORD")
            signing.key = secret("SIGNING_KEY")
            signing.password = secret("SIGNING_PASSWORD")

            // Publish manually from this link https://central.sonatype.com/publishing
            allowMavenCentralSync = false
        }
        localSpec {}
    }
}

dependencies {}
