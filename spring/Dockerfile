FROM registry.access.redhat.com/ubi8/openjdk-11:1.10-10.1638383051
EXPOSE 8082

ADD build/libs/spring.jar /opt/spring.jar

CMD ["java", "-jar", "/opt/spring.jar", "-Xmx1G"]
