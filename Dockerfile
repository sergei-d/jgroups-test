FROM openjdk:8-jre-alpine3.9

EXPOSE 7800-7900:7800-7900 9000-9100:9000-9100

WORKDIR /app

COPY target/jgroups-test-1.0-SNAPSHOT.jar ./demo.jar

# set the startup command to execute the jar
CMD ["java", "-Djava.net.preferIPv4Stack=true", "-Djgroups.bind_addr=127.0.0.1", "-jar", "demo.jar"]