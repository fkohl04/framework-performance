FROM registry.access.redhat.com/ubi8/openjdk-11:1.10-10.1638383051
EXPOSE 8081

ADD build/libs/ktor-0.0.2-all.jar /opt/ktor.jar

CMD ["java", "-jar", "/opt/ktor.jar", "-Xmx512m"]
