package mockserver.controller

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlinx.coroutines.delay
import mockserver.shared.extension.getPropertyOrNull
import kotlin.random.Random

fun Application.configureRouting() {

    val delay = getPropertyOrNull("delay")?.toLong() ?: 1000

    routing {
        route("/") {
            get {
                delay(delay)
                call.respond(Random.nextInt(0, 100000))
            }
        }
    }
}
