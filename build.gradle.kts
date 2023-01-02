plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.3.5"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.elliepotato"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()

    /* Paper */
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")
}

subprojects {
    apply(plugin = "io.papermc.paperweight.userdev")

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()

        /* Paper */
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    dependencies {
        paperDevBundle("1.18.2-R0.1-SNAPSHOT")
    }
}
