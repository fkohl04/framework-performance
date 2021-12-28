import fetch from 'node-fetch';
import http from 'http';
import url from 'url';
import prometheus from 'prom-client';

const register = new prometheus.Registry()

register.setDefaultLabels({
    app: 'node'
})

prometheus.collectDefaultMetrics({ register })

const server = http.createServer(async (req, res) => {
    const route = url.parse(req.url).pathname
    const thirdPartyUrl = process.env.THIRD_PARTY_URL || "http://localhost"

    if (route === '/metrics') {
        res.setHeader('Content-Type', register.contentType)
        res.end(await register.metrics())
    } else {
        (await fetch(thirdPartyUrl +":8080")).text().then( it =>
            res.end("Fetched third party result:" + it)
        )
    }
})

server.listen(8086)
