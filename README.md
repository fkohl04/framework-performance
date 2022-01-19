# framework-performance

This repository serves as base for a blog entry. If you want to retrace the explained testing result
see [this chapter.](#how-to-execute-the-test--not-part-of-blog-entry)

## Motivation

During the development of distributed applications their performance is always a crucial part. Every application is
developed to serve a certain purpose which in most cases consists of processing tasks. These tasks can be of various
types, like processing data or serving network requests. In all cases we can regard the performance of an application as
the amount of workload it can handle in a given time interval depending on the consumed or needed resources.

What to do when an application does not deliver the required performance? What if an application was developed to
process a certain amount of tasks each second, but it simply does not. Or the response time of our application becomes
under certain load so high that it results in bad user experience. If you have encountered such a situation before, some
of you may have discussed the following questions:

1. Can we deploy more instances of our application?

   Sometimes this is possible! Especially applications that are developed for cloud infrastructure can be easily scaled
   up and down so that the sum of deployed services has exactly the desired performance. But multiple instances also
   require the multiple of resources and resources are expensive or sometimes not even available. On top of this, this
   solution is not improving the performance of the application itself.

2. Are we able to improve the existing processing logic?

   Is there any potential for parallelization, is there any possibility for caching mechanisms, ...? The refactoring of
   existing logic should be a continuous process during development of any software. This also includes the continuous
   search for performance issues. Each issue you fix and each improvement you make is a future-oriented step and the
   best means when trying to build a long term solution for performance problems. But having to search with limited time
   for possible improvements in a complex system can be a hard, cumbersome and a maybe impossible task.

3. Should we have chosen a different framework 2 years ago, when we have started implementing this application?

   Maybe!

In this article, we want to investigate and concentrate on the third question. How relevant is the choice of a framework
when trying to achieve a certain performance? How big are the differences between currently commonly used frameworks?

## Test setup

### System

Let's think about a possible setup which we can use to compare the performance of different frameworks.

Applications of distributed system are often part of complex service structures. It is very common that a service uses a
database, calls other services that are under our control (Internal Dependency), but also uses endpoints from third
parties which we do not have control over and which can be very slow (External Dependency). We always have to mind that
when measuring the performance of a deployed application we are also measuring all systems the application relies on.

![](./assets/ExampleForComplexSystem.png)

Trying to compare the performance of frameworks in such a system would be very hard, because there are simply too many
parameters. In order to eliminate any side effect and achieve comparability, we will reduce the test setup to the
smallest possible constellation that still represents the built-in networking capabilities, used threading models and
load handling capabilities of the tested frameworks. For this we will reduce the count of dependencies to a single
external dependency.

![](./assets/DependenciesForPerformanceTest.png)

The test setup is now easy enough for a comparison. But we also have to make sure that the test is meaningful by
assuring that we are really measuring the performance of the service under test. This performance shall not be
influenced by our test setup:

- The service shall not be influenced by the system it runs on

  If multiple services are deployed in parallel, make sure they are not influencing each other. Repeating certain parts
  of the test with an isolated deployment shows that this is not the case. A performance test consumes a lot
  computational power and memory. The system resources have to be monitored during the test to assure the system or
  computer is not overloaded.

- The service shall not be limited by the performance of the mock service

  Overrating the capabilities of mock services is a known pitfall for performance testing. To overcome this we will do a
  little technical adjustment and raise the count of instances of the mocked service to 3. A load test against the mock
  service setup assures that it is performant enough to not influence the performance of the service under test.

![](./assets/DetailedDependenciesForPerformanceTest.png)

The functionality of the service under test consists of a call to the mocked third party dependency. When called, the
mock service will wait for 1 second and then respond with a random number. The service under test will forward this
answer.

![](./assets/SequenceForPerformanceTests.png)

All services will be started inside a docker container. We will use Gatling as a load testing client. It will call the
service under test with a certain amount of requests per second. Apart from the described functional endpoint that will
be used in the test, each service provides a further endpoint that serves service metrics to a Prometheus client.

## Candidates

I did a survey among my valued colleagues to find out which JVM frameworks they have encountered in productive systems.
To this list I added the JavaScript Runtime Node.js to have a little comparison to the outside of the JVM world.

We made our test setup as simple as possible to have fewer parameters and achieve comparability. We are creating our
services under exactly the same principle and will only implement the most simple application serving our needs without
any performance relevant adjustment or improvement. In general, I tried to deduce every service from the GettingStarted
tutorials of the framework websites to get exactly that impression of the service, the framework creators are
presenting.

### Spring

[Spring](https://spring.io/) is one of the most commonly used JVM frameworks, initially released 2002 and open source.
It is available in a blocking servlet stack and a non-blocking reactive stack. We will test both of them separately.

### KTOR

[KTOR](https://ktor.io/) is a framework for asynchronous client and server applications that promises to be lightweight,
flexible, simple and fun. Release of first version in 2018 by Jetbrains. Written completely in Kotlin and built up on
its coroutines. Ktor offers the possibility to [choose the underlying http engine](https://ktor.io/docs/engines.html).
We will test it with a Netty and with the coroutine based CIO Engine.

### Vertx

[Vertx](https://vertx.io/) is developed by Eclipse and was just released in 2021. It promises to be flexible, resource
efficient and enable writing non-blocking code without unnecessary complexity. Vertx is described as "a toolkit, not a
framework", which underlines its flexibility on the one hand, but also indicates it has to be configured to a certain
degree. Indeed, Vertx was the only service where I had to do a little performance influencing adjustment to make it
comparable to the other services: Set the number of verticles and set the max connection count of the HttpClient

### Micronaut

[Micronaut](https://micronaut.io/) is a framework for light weight and modular applications. It keeps the startup time
and memory footprint low among under things by avoiding reflection. It was developed by the Micronaut Foundation.

### Node

In contrast to all other JVM based frameworks we will also test a [Node.js](https://nodejs.org/en/about/) server which
is an asynchronous and event driven JavaScript runtime. The special part is that node is executing the event and network
handling on a single thread.

## Test Execution

We will test our candidates in three disciplines

1. How many requests per second can they process without getting unresponsive?
2. How do they behave if the third party system gets slower and slower?
3. What resources are the deployed containers consuming during a test with an average size of load?

### 1. Requests per second

The Gatling client will call each service one after another starting with 100 users over three minutes. If the rate of
successful calls is greater than 90% the test will be repeated with user count raised by 100 until the service gets
unresponsive. For further details please
the [script that executes the load test](https://github.com/fkohl04/framework-performance/blob/main/performancetest/runTillFailure.sh)
and
the [gatling test](https://github.com/fkohl04/framework-performance/blob/main/performancetest/src/gatling/scala/scenarios/BasicSimulation.scala)
itself.

#### Result

| Average response time | 100 | 200 | 300 | 400 |  500 |  600 |  700 |  800 |  900 |  1000 | 1100 |
|-----------------------|:---:|:---:|:---:|:---:|:----:|:----:|:----:|:----:|:----:|:-----:|:----:|
| Vertx                 |  1s |  1s |  1s |  1s |  1s  |  1s  |  1s  | 3.1s | 4.1s | 7.46s |   X  |
| Ktor-Netty            |  1s |  1s |  1s |  1s |  1s  |  1s  | 1.3s | 5.7s |   X  |       |      |
| Ktor-CIO              |  1s |  1s |  1s |  1s |  1s  | 1.1s |   X  |      |      |       |      |
| Micronaut             |  1s |  1s |  1s |  1s |  1s  | 1.4s |   X  |      |      |       |      |
| Node                  |  1s |  1s |  1s |  1s |  2.9 |   X  |      |      |      |       |      |
| Spring-Reactive       |  1s |  1s |  1s |  1s | 2.1s |   X  |      |      |      |       |      |
| Spring                |  1s |  1s |  X  |     |      |      |      |      |      |       |      |

![](./assets/result.png)

#### Observations

1. When a service is able to handle a certain user count the response time is near to the configured delay of the
   mockservice and the request rate is near to the count of requests per second. Before a service becomes unresponsive
   it can be observed that the response times are raising strongly and the request rate drops.
2. For the Spring service the thread count is equal to the requests rate during all successful test runs. For all other
   services the JVM thread count is nearly not changing during the test.
   ![](./assets/threads.png)

#### Reasoning

The Spring Service is the only one working with a blocking threading model. Each incoming call is processed by a
dedicated JVM thread. The underlying Tomcat http engine works with a fixed count of 200 threads. With this it is just
logical that it can serve 200 requests per second. Then why not just raising the count of threads? Of course maintaining
threads costs resources. More details about this can be observed in the third test.

For all other services it is clearly visible that they are working with a non-blocking threading model since the
requests rate is much higher than the count of active threads would allow it to be. This seems to work a bit better for
micronaut and ktor than for spring reactive and node. Vertx is the clear winner. At 700 req/sec it still responds nearly
as fast as the mocked service. With higher request counts the response times of Vertx get noticeably higher, but it
stays responsive up until impressing 1000 req/sec.

### 2. Very slow third party clients

### 3. Resource consumption

The Gatling client will call each service one after another with a certain rate of calls per second. We are using
cadvisor which exposes metrics about active containers as prometheus metrics, to compare the resource consumption of the
different containers. Before each test we will restart all containers, so that the resource metrics are not influenced
by earlier executions. Please note: For this very test we raised the thread count of the spring service to 500.

#### Result

| Max memory usage |   200   |   400   |    500   |
|------------------|:-------:|:-------:|:--------:|
| Vertx            | 376 MiB | 385 MiB |  424 MiB |
| Ktor-Netty       | 405 MiB | 434 MiB |  510 MiB |
| Ktor-CIO         | 417 MiB | 604 MiB |  898 MiB |
| Micronaut        | 472 MiB | 602 MiB |  510 MiB |
| Node             |  93 MiB | 113 MiB |  391 MiB |
| Spring-Reactive  | 509 MiB | 480 MiB |  1.4 GiB |
| Spring           | 596 MiB | 910 MiB | 1.05 GiB |

| CPU Time        | 200      | 400      | 500      |
|-----------------|----------|----------|----------|
| Vertx           | 32.6 s   | 43.4 s   | 1.14 min |
| Ktor-Netty      | 1.22 min | 1.82 min | 2.52 min |
| Ktor-CIO        | 1.33 min | 2.89 min | 2.72 min |
| Micronaut       | 1.33 min | 1.74 min | 2.53 min |
| Node            | 57.3 s   | 2.02 min | 2.87 min |
| Spring-Reactive | 1.17 min | 1.51 min | 3.15 min |
| Spring          | 2.02 min | 1.82 min | 2.38 min |

#### Observations

1.

#### Reasoning

## Resume

After all these numbers let us come back to our initial question. Many question during the development of software can
not be answered generally and are very dependent on the use case. Which framework to use and whether to use a
non-blocking or a blocking one is surely one of them. But no worries, the question we want to answer is a different one:
Are there differences between the performance of currently commonly used (JVM) frameworks? This question we can answer
with a clear: Yes.

Especially when you have strict requirements like
- a high request load
- slow third party dependencies
- limited resources

the choice of a framework can make a big difference. A non-blocking framework is not the one simply solution for all 
these challenges, but surely some of them are easier to overcome when choosing one.

An often read counterargument against reactive programming and non-blocking frameworks is, that the code becomes hard to
read, to maintain and to understand. And this may be true, when one tries to write
[stream-like instructions](spring-webflux/src/main/kotlin/spring/demo/demo/controller/Controller.kt) for the publisher
schema of Spring-Reactive. At that point I want to emphasize an in my opinion big advantage of Kotlin and its coroutines:
the asynchronous/non-blocking code
looks [nearly like synchronous/blocking code](https://kotlinlang.org/docs/async-programming.html#coroutines). You don't
have to learn or get used to completely new patterns, but can just write code that is easy to read and has the potential
to deliver a high performant result by being asynchronous. As written above Ktor is written completely in Kotlin and
built upon coroutines. But also all the other presented frameworks have a support for Kotlin + coroutines.

## How to execute the test ( Not part of blog entry)

All tested services are available as docker containers. To start the test environment, build all jars by executing

```shell
./gradlew build 
```

and then build the docker images and start the containers by running the [docker-compose file](docker-compose.yml).

The performance test used in this article is the gatling
simulation [ConstantReqPerSecSimulation](performancetest/src/gatling/scala/scenarios/ConstantReqPerSecSimulation.scala).
To test all applications one after another with different user counts you can use the
script [runAllTest.sh](performancetest/runAllTest.sh).
