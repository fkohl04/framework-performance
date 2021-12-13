package spring.demo.demo.datasource

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import spring.demo.demo.config.ThirdPartyConfig

@Configuration
class ThirdPartyClientConfig {

    @Bean
    fun thirdPartyWebClient(thirdPartyConfig: ThirdPartyConfig): WebClient {
        return WebClient.create("${thirdPartyConfig.url}:${thirdPartyConfig.port}")
    }

}
