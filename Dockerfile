FROM openjdk:8u171-jre-alpine

EXPOSE 8080
RUN mkdir -p /usr/app
WORKDIR /usr/app
COPY ./target/java-maven-app-1.1.0-SNAPSHOT.jar /usr/app/java-maven-app-1.1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "java-maven-app-1.1.0-SNAPSHOT.jar"]
