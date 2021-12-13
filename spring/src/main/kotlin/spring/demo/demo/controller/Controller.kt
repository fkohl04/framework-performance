package spring.demo.demo.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.client.RestTemplate
import spring.demo.demo.config.ThirdPartyConfig


@Controller
class Controller {

    @Autowired
    private lateinit var thirdPartyConfig: ThirdPartyConfig

    val restTemplate = RestTemplate()

    @GetMapping("/")
    fun getRandom(): ResponseEntity<String> {
        val serverResponse =
            restTemplate.getForEntity("${thirdPartyConfig.url}:${thirdPartyConfig.port}", Int::class.java).body
        return ResponseEntity.ok("Fetched third party result:$serverResponse")
    }
}
