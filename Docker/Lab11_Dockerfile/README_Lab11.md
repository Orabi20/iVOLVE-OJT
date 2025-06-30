
# Lab 11: Run Java Spring Boot App in a Container

This lab demonstrates how to run a Java Spring Boot application inside a Docker container using both pre-built `.jar` and multi-stage Docker builds.

---

## 🧩 Objective

Build and deploy a Spring Boot app in a Docker container using two approaches:
- Build the app **before** containerizing
- Build the app **inside** a Dockerfile (multistage)

---

## 📦 Step-by-Step Instructions

### 1. Clone the Project
```bash
git clone https://github.com/Ibrahim-Adel15/Docker-1.git
cd Docker-1
```

---

## 🚀 Approach 1: Multistage Dockerfile (Build Inside Docker)

### Dockerfile
```Dockerfile
# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

# Stage 2: Run
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Commands
```bash
docker build -t springboot-app .
docker run -d -p 8080:8080 --name springboot-container springboot-app
```

---

## 🧪 Test the App
```bash
curl http://localhost:8080
```
Or open in your browser: `http://localhost:8080`

---

## 🛑 Stop and Remove the Container
```bash
docker stop springboot-container
docker rm springboot-container
```

---

## 🔁 Approach 2: Build First, Then Containerize

### Pre-build the App
```bash
mvn clean package
```

### Simplified Dockerfile
```Dockerfile
FROM openjdk:17
WORKDIR /app
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Then:
```bash
docker build -t springboot-app .
docker run -d -p 8080:8080 --name springboot-container springboot-app
```

---

## 📌 Notes
- `ENTRYPOINT` is used to lock in the execution command.
- `CMD` can be added to provide default arguments.
- The multistage build is cleaner and more production-ready.

---

## ✅ Outcome

You successfully:
- Built a Spring Boot app using Maven
- Containerized it using Docker
- Tested it using `curl` or browser access
