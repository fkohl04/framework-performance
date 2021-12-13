package spring.demo.demo.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


@RestController
class Controller {

    @Autowired
    private lateinit var thirdPartyWebClient: WebClient

    @GetMapping("/")
    fun fetchThirdPartyData(): Mono<String> {
        return thirdPartyWebClient.get()
            .exchangeToMono { it.bodyToMono(Int::class.java) }
            .map { serverResponse ->
                "Fetched third party result:$serverResponse"
            }
    }
}
