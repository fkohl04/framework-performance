plugins {
    id("io.gatling.gradle") version "3.6.1"
    scala
}

description = "Performance test"

repositories {
    mavenCentral()
}

dependencies {
    gatling("org.scala-lang:scala-library:2.13.6")
}

tasks {
    gatlingRun {
        // Load test executions shall be repeated every single time and can never be UP-TO-DATE
        outputs.upToDateWhen { false }
    }
}
