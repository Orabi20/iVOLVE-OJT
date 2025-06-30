# Lab 12: Multi-Stage Build for a Java Application Using Docker

This lab demonstrates how to build and run a Java application using Docker multi-stage builds to reduce image size and separate build/runtime environments.

---

## 🧪 Lab Steps

### 1. Clone the Application Code

```bash
git clone https://github.com/Ibrahim-Adel15/Docker-1.git
cd Docker-1
```

---

### 2. Create Multi-Stage Dockerfile

Create a file named `Dockerfile` in the project root with the following content:

```dockerfile
# ===== Stage 1: Build =====
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ===== Stage 2: Run =====
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

---

### 3. Build the Docker Image

```bash
docker build -t java-multistage-app .
```

---

### 4. Run the Docker Container

```bash
docker run -d -p 8080:8080 java-multistage-app
```

---

### 5. Test the Application

Open your browser or use `curl`:

```bash
curl http://localhost:8080
```

---

### 6. Stop and Delete the Container

```bash
docker ps                   # Get the container ID
docker stop <container_id>
docker rm <container_id>
```

---

## ✅ What You Learned

- How to use **multi-stage builds** in Docker to separate build and runtime environments.
- How to reduce image size by avoiding bundling build tools (like Maven) in the final image.
- How to build and run a Java app inside a Docker container.
