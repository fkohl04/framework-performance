package mockserver

import io.ktor.application.Application
import mockserver.controller.configureRouting
import mockserver.shared.plugins.configureMicrometer
import mockserver.shared.plugins.configureSerialization

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureMicrometer()
    configureRouting()
}
