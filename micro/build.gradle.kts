val kotlin_version: String by project

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    id("org.jetbrains.kotlin.kapt") version "1.6.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.1"
    id("io.micronaut.application") version "3.1.1"
}

version = "0.1"
group = "micro"

repositories {
    mavenCentral()
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("micro.*")
    }
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
    implementation("jakarta.annotation:jakarta.annotation-api")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")

    kapt("io.micronaut:micronaut-http-validation")


    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")

    implementation("io.micronaut.reactor:micronaut-reactor-http-client:2.1.1")

    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlin_version}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlin_version}")

    implementation("io.micronaut:micronaut-management")

    runtimeOnly("io.micronaut.reactor:micronaut-reactor:2.1.1")
    runtimeOnly("ch.qos.logback:logback-classic")


    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

}


application {
    mainClass.set("micro.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}
