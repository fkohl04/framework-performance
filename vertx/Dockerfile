FROM registry.access.redhat.com/ubi8/openjdk-11:1.10-10.1638383051
EXPOSE 8087

ADD build/libs/vertx-1.0.0-fat.jar /opt/vertx.jar

CMD ["java", "-jar", "/opt/vertx.jar", "-Xmx2G"]
