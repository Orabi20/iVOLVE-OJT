# Lab 9: Build Java Application using Gradle

## Objective
This lab demonstrates how to build and run a Java application using Gradle. You will clone a repository, run unit tests, generate a JAR file (artifact), run the application, and verify it works.

---

## Steps

### 1. Install Gradle

#### CentOS:
```bash
sudo yum install -y gradle
```

#### Ubuntu:
```bash
sudo apt update
sudo apt install -y gradle
```

#### Verify Gradle Installation:
```bash
gradle -v
```

---

### 2. Install Java (if not already installed)

```bash
# CentOS / RHEL
sudo yum install java-11-openjdk -y

# Ubuntu / Debian
sudo apt install openjdk-11-jdk -y

# Verify installation
java -version
```

---

### 3. Clone Source Code

```bash
git clone https://github.com/Ibrahim-Adel15/build1.git
cd build1
```

---

### 4. Run Unit Tests

```bash
gradle test
```

Expected output should include:
```
BUILD SUCCESSFUL
```

---

### 5. Build the Application

```bash
gradle build
```

This will generate the JAR file at:
```
build/libs/ivolve-app.jar
```

---

### 6. Run the Application

```bash
java -jar build/libs/ivolve-app.jar
```

---

### 7. Verify the Application

You should see output in the terminal indicating that the application is working as expected.

---