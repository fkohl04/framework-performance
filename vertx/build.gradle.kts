import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin ("jvm") version "1.6.10"
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "vertx"
version = "1.0.0"

repositories {
  mavenCentral()
}

val vertxVersion = "4.2.3"

val mainVerticleName = "vertx.demo.MainVerticleKt"

application {
  mainClass.set(mainVerticleName)
}

tasks{
  shadowJar {
    archiveClassifier.set("fat")
    manifest {
      attributes(Pair("Main-Class", mainVerticleName))
    }
  }
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-config")
  implementation("io.vertx:vertx-web")
  implementation("io.netty:netty-all:4.1.68.Final.")
  implementation("io.vertx:vertx-lang-kotlin")
  implementation("io.vertx:vertx-micrometer-metrics:4.2.3")
  implementation("io.micrometer:micrometer-registry-prometheus:1.8.1")
  implementation(kotlin("stdlib-jdk8"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"
