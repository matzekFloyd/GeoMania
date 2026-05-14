plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "geomania"
version = (findProperty("geoVersion") as String?) ?: "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.processing:core:3.3.7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass.set("geomania.GeoMania")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "geomania.GeoMania")
    }
}

tasks.shadowJar {
    archiveBaseName.set("GeoMania")
    archiveClassifier.set("all")
    archiveVersion.set(project.version.toString())
    mergeServiceFiles()
    manifest {
        attributes("Main-Class" to "geomania.GeoMania")
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
