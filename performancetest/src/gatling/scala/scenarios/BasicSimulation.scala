package scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.protocol.HttpProtocolBuilder.toHttpProtocol

import scala.concurrent.duration._
import scala.language.postfixOps

class BasicSimulation extends Simulation {
  val userCount: Int = sys.env.getOrElse("USER_COUNT", "100").toInt
  val duration: Int = sys.env.getOrElse("DURATION", "60").toInt
  val url: String = sys.env.getOrElse("SUT_URL", "http://localhost:8081")

  val httpProtocol: HttpProtocolBuilder = http
      .baseUrl(url).shareConnections

  val scn: ScenarioBuilder = scenario(s"Testing $url")
    .exec {
      http(s"GET $url")
        .get("/")
        .check(status.is(200))
    }

  setUp(scn.inject(constantConcurrentUsers(userCount)  during (duration seconds))
    .protocols(httpProtocol.build))
}
