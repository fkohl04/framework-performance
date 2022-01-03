# framework-performance

## Motivation

During the development of distributed applications their performance is always a crucial part. Every application is
developed to serve a certain purpose which in most cases consists of processing tasks. These tasks can be of various
types, like processing data or serving network requests. In all cases we can regard the performance of an application as
the amount of workload it can handle in a given time interval depending on the consumed or needed resources.

A performant application needs less time and resources as a not performant application, leading to saving money and
computational resources. In the case of an application serving web requests its performance is in most cases equivalent
to the response time of the application, which is a crucial part of the user experience.

What to do when an application does not deliver the required performance? Let's imagine an application was developed to
process a certain amount of tasks each second, but it is simply slower. Or the response time of our application is so
high that it results in bad user experience. Some of you may have discussed the following questions:

1. Can we deploy more instances of our application?

   Sometimes this is possible! Especially applications that are developed for cloud infrastructure is can be easily
   scaled up and down so that the sum of deployed application has exactly the desired performance. But multiple
   instances also require the multiple of resources and resources are expensive or sometimes not even available. On top
   of this, this solution is not improving the performance of the application itself.

2. Are we able to improve the existing processing logic?

   Is there any potential for parallelization, is there any possibility for caching mechanisms, ...? The refactoring of
   existing logic should be a continuous process during development of any software. This refactoring also includes
   performance relevant improvements. Each improvement is future-oriented and improving logic is the best means when
   trying to build a long term solution for performance problems. But having to search with limited time for possible
   improvements in a complex system can be a hard, cumbersome and maybe impossible task.

3. Should we have chosen a different framework 2 years ago, when we have started implementing this application?

   Maybe!

In this article, we want to investigate and concentrate on the third question. Is the chose of a framework relevant for
the performance I want to achieve?

## Test setup

### System

Let's think about a possible setup which we can use to compare the performance of different frameworks.

Applications of distributed system are often part of complex service structures. When measuring the performance of a
deployed application we are also measuring all systems the application relies on.
![](./assets/ExampleForComplexSystem.png)

Trying to compare the performance of frameworks in such a system would be very hard, because there are simply to many
parameters . To eliminate any side effect and achieve comparability we will reduce the test setup to the smallest
possible constellation that still represents the built-in networking capabilities and load handling of the tested
frameworks.
![](./assets/DependenciesForPerformanceTest.png)

Like this the test setup is easy enough for a comparison, but we have to do a little technical adjustment. Since we want
to test the performance of the "Service under test" we have to make sure, that the performance is not influenced or
limited by the performance of the mock service. We can easily exclude this source of errors, by raising the count of
instances of the mocked service.
![](./assets/DetailedDependenciesForPerformanceTest.png)

We will use Gatling as a load testing client. It will call the service under test with a certain amount of parallel
users. When a call of a user finishes it will simply perform the next call.

The functionality of the service under test consists of a call to a service that shall represent a slow third party
dependency. The mockservice is a KTOR server running on Netty. When called, it will wait for 1s and then respond with a
random number.

![](./assets/SequenceForPerformanceTests.png)

Besides the described endpoint that will be used in the test each service provides a further endpoint to provide service
metrics to a Prometheus client.

### Test parameters

All services will be started inside a docker container. Called will call each service one after another starting with
100 up to 1000 users taking steps of 100 users. For further details please
the [script that executes the load test](https://github.com/fkohl04/framework-performance/blob/main/performancetest/runAllTest.sh)
and
the [gatling test](https://github.com/fkohl04/framework-performance/blob/main/performancetest/src/gatling/scala/scenarios/BasicSimulation.scala)
itself.

Todo constantConcurrentUsers vs requests per second

## Candidates

### Spring on Tomcat

The definition of classic Apache client

### Spring-Reactive on Netty

Call stack Apache client

### KTOR

from presentation Apache client

Apache client

### Node

## Result

![](./assets/result.png)

### Observations

1. The rate of executed requests is growing nearly linearly in regard of the user count for all three KTOR services. The
   rate of the KTOR service running on TomCat is slightly lower than the other ones.
2. The rate is capped at 200 req/sec for Spring on Tomcat and at 500 req/sec for Spring on Netty.
3. For the node service the rate of executed requests is growing in regard of the user count, but not linearly and far
   slower than the rate of the KTOR services.
4. Spring on Tomcat nearly has a constant response time regardless of the user count. The response times of all other
   services are growing in regard of the user count. Only regarding these the response times of the KTOR services are
   growing slowest.

### Reasoning

Spring Tomcat hat kalr eine feste Anzahl von Threads. Diese Sorgen für eine konstante response time, wären in der wirklichen Welt aber schlicht überlastet und würden zu Fehlern führen. Increasing of ThreadCount would result in increasing of resources.

Spring Netty kann durch task loop mit gleicher threadzahl mehrere task parallel bearbeiten kommt aber trotzdem an Grenzen, weil im Endeffekt immer noch ganze threads warten müssen.

KTOR Services können mit Koroutinen dafür sorgen, dass eigentliche jvm threads durchgängig arbeiten, während koroutinen beim warten blockiert sind. Dies sorgt für schnelle Response time um hohe durchgangsrate bei Gleichzeitig kleinem Speicherverbrauch

Node hervorheben, dass Node servcer super wenig speicher verbraucht

Todo, kann man speicher von node server manuell anheben?

## Resume

Auf alle Fälle asynchrones Framework wählen. Performance ist schnell doppelt so groß.

Kotlin Coroutines sind cool. Ktor ist ein Framework, das von Grund auf mit Kotlin und Coroutinen aufgebaut ist und hat daher riesiges Potential. 

Node hervorheben, dass für die Einfachheit sehr performant. Skaliert in unserem Test nicht ganz so gut, wie ein asynchroner JVM Service.

## Side Notes ( Not part of blog entry)

Problems with CIO Client

Result depends on waiting time of mock server

Where to find test results, how to execute tests? Part after Blog entry
