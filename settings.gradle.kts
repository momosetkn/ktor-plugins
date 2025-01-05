pluginManagement {
    val kotlinVersion: String by settings
    val ktorVersion: String by settings
    repositories {
        gradlePluginPortal()
    }
    plugins {
        kotlin("jvm") version kotlinVersion
        id("io.ktor.plugin") version ktorVersion
    }
}

rootProject.name = "ktor-plugins"

include("doma-statistics")
include("komapper-statistics")
