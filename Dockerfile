FROM openjdk:11
ARG JAR_FILE=target/university-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} university-latest.jar
ENTRYPOINT ["java", "-jar","university-latest.jar"]
EXPOSE 8080