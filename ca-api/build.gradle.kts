plugins {
    id("java")
}

group = "de.elliepotato"
version = "1.0-SNAPSHOT"

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}