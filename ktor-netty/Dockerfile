FROM registry.access.redhat.com/ubi8/openjdk-11:1.10-10.1638383051
EXPOSE 8082

ENV JAVA_OPTS="-Xmx256m"

ADD build/libs/ktor-netty-0.0.1-all.jar /opt/ktor-netty.jar

CMD ["java", "-jar", "/opt/ktor-netty.jar"]
