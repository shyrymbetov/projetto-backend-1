FROM openjdk:17-alpine
MAINTAINER InnLab KZ

ADD ./target/main-service-0.0.1-SNAPSHOT.jar /app/
ADD ./resources /app/resources
CMD ["java", "-jar", "/app/main-service-0.0.1-SNAPSHOT.jar", "--spring.config.name=bootstrap", "--spring.config.location=file:/app/resources/"]

EXPOSE 8080
