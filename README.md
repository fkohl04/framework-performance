# framework-performance

# Performance Test

What is performance? ...

## Motivation

When building distributed applications their performance is always a crucial part ...

What to do when an application does not deliver the required performance?

1. Deploying more instances - Sometimes this is possible! But multiple instances also require the multiple of resources
   and resources are expensive or sometimes not even available. Keine langfristige Lösung
2. Investigating and improving the processing logic. Potential for parallelization? Possibility for caching? The
   refactoring of existing logic should be a continuous process. This also includes refactoring of performance relevant
   improvements. Having to search for possible performance improvements in a complex system can be hard and cumbersome,
   but langfristig + zukunftsorientiert
3. Should we have chosen a different framework 2 years ago, when we have started implementing this application? Maybe!

In this article, we want to investigate and concentrate on the third question. Is the chose of a framework relevant for
the performance I want to achieve?

## Test setup

To eliminate any side effect we will reduce the test setup to the smallest possible constellation that still represents
the built-in networking capabilities and load handling of the tested framework.
![](./assets/DependenciesForPerformanceTest.png)

To test the performance we are using a Gatling client that will call a single endpoint of the service under test with a
certain amount of users per second over one minute. The functionality of the tested service consists of a call to a mock
server that shall represent a slow third party dependency.

![](./assets/SequenceForPerformanceTests.png)

The mockserver is a KTOR server running on a Netty. When called, it will wait for 200ms and then responds with a random number.

## Candidates

### Spring on Tomcat

The definition of classic
### Spring-Reactive on Netty

Call stack
### KTOR
 from presentation
## Result

## Resume
