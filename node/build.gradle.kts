plugins {
    id("com.github.node-gradle.node") version "3.0.1"
}

tasks.register<com.github.gradle.node.task.NodeTask>("run") {
    dependsOn("npmInstall")
    script.set(file("src/app.js"))
    ignoreExitValue.set(false)
    workingDir.set(projectDir)
    inputs.dir("src")
    outputs.upToDateWhen {
        false
    }
}
