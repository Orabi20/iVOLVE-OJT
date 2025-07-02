# Lab 17: Scan Docker Image with Trivy

## 1. Install Trivy
Follow the guide at: [Trivy Installation Guide](https://trivy.dev/latest/getting-started/installation/)

### For Linux:
```bash
sudo apt install wget -y
wget https://github.com/aquasecurity/trivy/releases/latest/download/trivy_0.50.1_Linux-64bit.deb
sudo dpkg -i trivy_0.50.1_Linux-64bit.deb
trivy --version
```

## 2. Clone the Application Repository
```bash
git clone https://github.com/Ibrahim-Adel15/Docker-1.git
cd Docker-1
```

## 3. Create Dockerfile
Create a `Dockerfile` with the following content:
```Dockerfile
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/demo.jar"]
```

## 4. Build Docker Image
Replace `your-image-name` with your DockerHub username/image name.
```bash
docker build -t your-image-name .
# Example:
docker build -t ahmedorabi/demo-app .
```

## 5. Scan Image with Trivy
```bash
trivy image --format json --output trivy-report.json your-image-name
# Example:
trivy image --format json --output trivy-report.json ahmedorabi/demo-app
```

## 6. Push Image to DockerHub
```bash
docker login
docker push your-image-name
# Example:
docker push ahmedorabi/demo-app
```

## ✅ Summary Checklist

| Task                         | Status |
|------------------------------|--------|
| Install Trivy                | ✅     |
| Clone app repo               | ✅     |
| Write Dockerfile using Maven| ✅     |
| Build image                  | ✅     |
| Scan image with Trivy        | ✅     |
| Save report as JSON          | ✅     |
| Push image to DockerHub      | ✅     |
