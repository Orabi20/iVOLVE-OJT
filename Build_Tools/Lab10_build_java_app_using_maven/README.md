
# Lab 10: Build Java App Using Maven

This lab demonstrates how to build and run a simple Java application using Apache Maven.

## Lab Steps

### 1. Install Maven

**On CentOS:**
```bash
sudo dnf install maven -y
```

### 2. Clone the Source Code

```bash
git clone https://github.com/Ibrahim-Adel15/build2.git
cd build2
```

### 3. Run Unit Test

```bash
mvn test
```
<img width="627" alt="Lab10_1" src="https://github.com/user-attachments/assets/f9d5f512-8d73-48ef-bb6a-a7336d822924" />

### 4. Build the App (Generate Artifact)

```bash
mvn package
```
<img width="632" alt="Lab10_2" src="https://github.com/user-attachments/assets/bca98146-0f39-4563-8b66-0ed1f9a3c404" />

> This will generate the artifact at:
> `target/hello-ivolve-1.0-SNAPSHOT.jar`

### 5. Run the App

```bash
java -jar target/hello-ivolve-1.0-SNAPSHOT.jar
```
<img width="629" alt="Lab10_3" src="https://github.com/user-attachments/assets/392988e2-a1e4-4382-8d68-53a8b2c3f726" />

### 6. Verify the App is Working

If the output shows the expected message (e.g., "Hello iVolve!"), the application works correctly.
