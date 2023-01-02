rootProject.name = "commandalias"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}
include("ca-api")
include("ca-plugin")
