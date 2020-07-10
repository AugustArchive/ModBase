import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "4.0.1"
    application
    java
}

val verTag = Version(1, 0, 0, 0)
group = "com.thattonybo"
version = "1.0.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.slf4j:slf4j-api:1.7.25")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    named<ShadowJar>("shadowJar") {
        mergeServiceFiles()
        archiveFileName.set("ModBase.jar")
        manifest {
            attributes(mapOf(
                    "Manifest-Version" to "1.0.0",
                    "Main-Class" to "com.thattonybo.modbase.Boostrap"
            ))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

application {
    mainClassName = "com.thattonybo.modbase.Bootstrap"
}

class Version(private val major: Int, private val minor: Int, private val revision: Int, private val buildVer: Int) {
    // Example: "1.0.0.0"
    fun string(): String = "$major.$minor.$revision.$buildVer"
    fun commit(): String = runCommand("git rev-parse HEAD")
}

fun runCommand(command: String, workingDir: File = file("./")): String {
    val parts = command.split("\\s".toRegex())
    val process = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

    process.waitFor(1, TimeUnit.MINUTES)
    return process.inputStream.bufferedReader().readText().trim()
}