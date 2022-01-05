package micro.datasource

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import jakarta.inject.Singleton
import micro.config.ThirdPartyConfig
import reactor.core.publisher.Mono
import java.net.URI

@Singleton
class ThirdPartyClient(
    @param:Client private val httpClient: HttpClient,
    thirdPartyConfig: ThirdPartyConfig
) {
    private val uri: URI = UriBuilder.of("${thirdPartyConfig.url}:${thirdPartyConfig.port}").build()

    fun fetchReleases(): Mono<String> = Mono.from(
        httpClient.retrieve(HttpRequest.GET<String>(uri))
    )

}
