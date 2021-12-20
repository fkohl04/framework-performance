package ktor.modules.performance.client

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import ktor.shared.extension.getPropertyOrEmptyString

fun Application.configureRouting() {

    val serverUrl = getPropertyOrEmptyString("server.url")
    val serverPort = getPropertyOrEmptyString("server.port")

    val thirdPartyClient = ThirdPartyClient(serverUrl, serverPort)

    routing {
        route("/") {
            get {
                thirdPartyClient.fetchData().also { serverResponse ->
                    call.respond("Fetched third party result:$serverResponse")
                }
            }
        }
    }
}
