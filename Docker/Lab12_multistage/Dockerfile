FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY Docker-1/ .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
