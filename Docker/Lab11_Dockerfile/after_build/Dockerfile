FROM maven:3.9.10-eclipse-temurin-11-alpine

WORKDIR /app

COPY Docker-1/ .

RUN mvn package

CMD ["java", "-jar", "target/demo-0.0.1"]

EXPOSE 8080

