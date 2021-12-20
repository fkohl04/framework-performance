FROM registry.access.redhat.com/ubi8/openjdk-11:1.10-10.1638383051
EXPOSE 8080

ADD build/libs/mockserver-0.0.1-all.jar /opt/mockserver.jar

CMD ["java", "-jar", "/opt/mockserver.jar", "-Xmx4G"]
