
# Lab 10: Build Java App Using Maven

This lab demonstrates how to build and run a simple Java application using Apache Maven.

## Lab Steps

### 1. Install Maven

**On CentOS:**
```bash
sudo dnf install maven -y
```

**On Ubuntu:**
```bash
sudo apt update
sudo apt install maven -y
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

### 4. Build the App (Generate Artifact)

```bash
mvn package
```

> This will generate the artifact at:
> `target/hello-ivolve-1.0-SNAPSHOT.jar`

### 5. Run the App

```bash
java -jar target/hello-ivolve-1.0-SNAPSHOT.jar
```

### 6. Verify the App is Working

If the output shows the expected message (e.g., "Hello iVolve!"), the application works correctly.
