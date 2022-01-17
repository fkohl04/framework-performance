package mockserver.shared.extension

import io.ktor.application.Application

fun Application.getPropertyOrNull(property: String): String? {
    return environment.config.propertyOrNull(property)?.getString()
}
