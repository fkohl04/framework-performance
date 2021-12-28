package ktor

import io.ktor.application.Application
import ktor.controller.configureRouting
import ktor.shared.plugins.configureMicrometer
import ktor.shared.plugins.configureSerialization

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureMicrometer()
    configureRouting()
}
