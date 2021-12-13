package ktor.modules.performance.client

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import ktor.shared.extension.getPropertyOrEmptyString
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ThirPartyClient(private val serverUrl: String, private val serverPort: String) {

    private val client = HttpClient()

    suspend fun fetchData() = client.get<HttpResponse>("$serverUrl:$serverPort")
        .readText()
        .toInt()
}
