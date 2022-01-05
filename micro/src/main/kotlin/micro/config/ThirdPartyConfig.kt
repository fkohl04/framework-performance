package micro.config

import io.micronaut.context.annotation.ConfigurationProperties


@ConfigurationProperties("third.party")
data class ThirdPartyConfig(
    var url: String = "",
    var port: String = ""
)
