package vertx.demo

import io.vertx.config.ConfigRetriever
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.micrometer.MicrometerMetricsOptions
import io.vertx.micrometer.PrometheusScrapingHandler
import io.vertx.micrometer.VertxPrometheusOptions

fun main() {
    val vertx = configureVertxWithPrometheus()
    vertx.deployVerticle(MainVerticle::class.java, DeploymentOptions().setInstances(4))
}

private fun configureVertxWithPrometheus() = Vertx.vertx(
    VertxOptions().setMetricsOptions(
        MicrometerMetricsOptions()
            .setPrometheusOptions(VertxPrometheusOptions().setEnabled(true))
            .setEnabled(true)
            .setJvmMetricsEnabled(true)
    )
)

class MainVerticle : AbstractVerticle() {

    override fun start() {

        val config = ConfigRetriever.create(vertx)

        lateinit var thirdPartyUrl: String
        lateinit var thirdPartyPort: String
        lateinit var port: String

        config.getConfig { json ->
            val result = json.result()
            thirdPartyUrl = result.getString("THIRD_PARTY_URL", "localhost")
            thirdPartyPort = result.getString("THIRD_PARTY_PORT", "8080")
            port = result.getString("PORT", "8087")
        }

        val router = vertx.configureRouter(thirdPartyPort, thirdPartyUrl)

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(port.toInt())
            .onSuccess { server ->
                println("HTTP server started on port " + server.actualPort())
            }
    }

    private fun Vertx.configureRouter(
        thirdPartyPort: String,
        thirdPartyUrl: String
    ): Router {
        val client = WebClient.create(this, WebClientOptions().setMaxPoolSize(500))
        val router = Router.router(this)

        router.route("/metrics").handler(PrometheusScrapingHandler.create())
        router.route("/").handler { context ->
            client.get(thirdPartyPort.toInt(), thirdPartyUrl, "/")
                .`as`(BodyCodec.string())
                .send { asyncResult ->
                    if (asyncResult.succeeded()) {
                        val body = asyncResult.result().body()
                        context.end("Fetched third party result:$body")
                    } else {
                        context.fail(500)
                    }
                }
        }
        return router
    }
}
