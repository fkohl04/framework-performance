import fetch from 'node-fetch';
import http from 'http';
import url from 'url';
import prometheus from 'prom-client';

const register = new prometheus.Registry()

register.setDefaultLabels({
    app: 'node'
})

prometheus.collectDefaultMetrics({ register })

const httpRequestTimer = new prometheus.Histogram({
    name: 'http_request_duration_seconds',
    help: 'Duration of HTTP requests in seconds',
    labelNames: ['route']
});
register.registerMetric(httpRequestTimer);

const server = http.createServer(async (req, res) => {
    const timer = httpRequestTimer.startTimer()
    const route = url.parse(req.url).pathname
    const thirdPartyUrl = process.env.THIRD_PARTY_URL || "http://localhost"

    if (route === '/metrics') {
        res.setHeader('Content-Type', register.contentType)
        res.end(await register.metrics())
        timer({route: "/metrics"})
    } else {
        (await fetch(thirdPartyUrl +":8080")).text().then( it => {
                res.end("Fetched third party result:" + it)
                timer({route: "/"})
            }
        )
    }
})

server.listen(8086)
