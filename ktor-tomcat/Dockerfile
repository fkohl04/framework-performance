FROM registry.access.redhat.com/ubi8/openjdk-11:1.10-10.1638383051
EXPOSE 8082

ADD build/libs/ktor-tomcat-0.0.1-all.jar /opt/ktor-tomcat.jar

CMD ["java", "-jar", "/opt/ktor-tomcat.jar", "-Xmx2G"]
