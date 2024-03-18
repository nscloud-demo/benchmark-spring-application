group = "com.github.gavlyukovskiy"
version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.9.23" apply false
    kotlin("plugin.spring") version "1.9.23" apply false
    id("org.springframework.boot") version "3.2.3" apply false
}

subprojects {
    tasks {
        withType<Test>().configureEach {
            useJUnitPlatform()
        }

        withType<JavaCompile> {
            targetCompatibility = "21"
        }

        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "21"
            }
        }

        withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage>().configureEach {
            // XXX Added docker://
            // XXX with digest?
            imageName.set(project.name)
            buildpacks.set(listOf("docker://gcr.io/paketo-buildpacks/adoptium@sha256:86d031571027360c4c35d31b1d37e7a1fc36c24770fa0400d8c2eac5c73863c2", "urn:cnb:builder:paketo-buildpacks/java"))
            environment.set(
                mapOf(
                    "BP_JVM_TYPE" to "JDK",
                    "BP_JVM_VERSION" to "21",
                    "BPL_JVM_HEAD_ROOM" to "10"
                )
            )
        }
    }
}
