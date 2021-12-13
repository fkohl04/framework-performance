package spring.demo.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("third.party")
class ThirdPartyConfig(
    val url: String,
    val port: String
)
