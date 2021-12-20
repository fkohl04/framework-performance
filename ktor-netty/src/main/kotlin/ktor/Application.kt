package ktor

import io.ktor.application.Application
import ktor.modules.performance.client.configureRouting
import ktor.shared.plugins.configureCallLogging
import ktor.shared.plugins.configureMicrometer
import ktor.shared.plugins.configureSerialization

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureCallLogging()
    configureMicrometer()
    configureRouting()
}
