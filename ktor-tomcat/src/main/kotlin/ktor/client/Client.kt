package ktor.client

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText

class ThirdPartyClient(private val serverUrl: String, private val serverPort: String) {

    private val client = HttpClient()

    suspend fun fetchData() = client.get<HttpResponse>("$serverUrl:$serverPort")
        .readText()
        .toInt()
}
