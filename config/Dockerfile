FROM openjdk:17-alpine
MAINTAINER InnLab KZ

ADD ./target/config-0.0.1-SNAPSHOT.jar /app/
ADD ./resources /app/resources
CMD ["java", "-jar", "/app/config-0.0.1-SNAPSHOT.jar", "--spring.config.name=application", "--spring.config.location=file:/app/resources/", "-Dprofile=prod"]

EXPOSE 8888
