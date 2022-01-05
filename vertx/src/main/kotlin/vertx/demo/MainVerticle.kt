package vertx.demo

import io.vertx.config.ConfigRetriever
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.micrometer.MicrometerMetricsOptions
import io.vertx.micrometer.PrometheusScrapingHandler
import io.vertx.micrometer.VertxPrometheusOptions


class MainVerticle : AbstractVerticle() {

    override fun start() {

        val config = ConfigRetriever.create(vertx)

        lateinit var thirdPartyUrl: String
        lateinit var thirdPartyPort: String
        lateinit var port: String

        config.getConfig { json ->
            val result = json.result();
            thirdPartyUrl = result.getString("THIRD_PARTY_URL", "localhost")
            thirdPartyPort = result.getString("THIRD_PARTY_PORT", "8080")
            port = result.getString("PORT", "8087")
        }

        val request: HttpRequest<String> = WebClient.create(vertx)
            .get(thirdPartyPort.toInt(), thirdPartyUrl, "/")
            .`as`(BodyCodec.string())

        val vertx = Vertx.vertx(
            VertxOptions().setMetricsOptions(
                MicrometerMetricsOptions()
                    .setPrometheusOptions(VertxPrometheusOptions().setEnabled(true))
                    .setEnabled(true)
            )
        )

        val router = Router.router(vertx)

        router.route("/metrics").handler(PrometheusScrapingHandler.create())

        router.route("/").handler { context ->
            request.send { asyncResult ->
                if (asyncResult.succeeded()) {
                    val body = asyncResult.result().body()
                    context.end("Fetched third party result:$body")
                } else {
                    context.fail(500)
                }
            }
        }

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(port.toInt())
            .onSuccess { server ->
                println("HTTP server started on port " + server.actualPort())
            }
    }
}
