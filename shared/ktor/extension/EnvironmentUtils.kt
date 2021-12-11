package shared.ktor.extension

import io.ktor.application.Application

fun Application.getPropertyOrEmptyString(property: String): String {
    return environment.config.propertyOrNull(property)?.getString() ?: ""
}
