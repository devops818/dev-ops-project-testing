FROM openjdk:8u171-jre-alpine

EXPOSE 8080
RUN mkdir -p /usr/app
COPY ./target/java-maven-app-*.jar /usr/app/
WORKDIR /usr/app

# ENTRYPOINT ["java", "-jar", ""]
CMD jave -jar java-maven-app-*.jar
