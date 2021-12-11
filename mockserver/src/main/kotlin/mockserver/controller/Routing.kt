package mockserver.controller

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlinx.coroutines.delay
import kotlin.random.Random

fun Application.configureRouting() {

    routing {
        route("/") {
            get {
                delay(200)
                call.respond(Random.nextInt(0, 100000))
            }
        }
    }
}
