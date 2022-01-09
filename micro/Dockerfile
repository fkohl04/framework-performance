FROM registry.access.redhat.com/ubi8/openjdk-11:1.10-10.1638383051
EXPOSE 8085

ADD build/libs/micro-0.1-all.jar /opt/micro.jar

CMD ["java", "-jar", "/opt/micro.jar", "-Xmx512m"]
