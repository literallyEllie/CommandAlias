plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "2.0.1" // Adds runServer and runMojangMappedServer tasks for testing
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2" // Generates plugin.yml
}

group = "de.elliepotato"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":ca-api"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    reobfJar {
        outputJar.set(layout.buildDirectory.file("libs/${project.name}.jar"))
    }
}


bukkit {
    name = "commandalias"
    main = "de.elliepotato.commandalias.CommandAliasPlugin"
    apiVersion = "1.18"
    author = "ellie"
    version = "1.0-SNAPSHOT"
}