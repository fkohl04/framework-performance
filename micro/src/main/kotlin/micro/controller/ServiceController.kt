package micro.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import micro.datasource.ThirdPartyClient
import reactor.core.publisher.Mono

@Controller("/")
class ServiceController(private val thirdPartyClient: ThirdPartyClient) {

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun fetch(): Mono<String> = thirdPartyClient.fetchReleases().map { "Fetched third party result:$it" }
}
