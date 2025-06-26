# Lab 9: Build Java Application using Gradle

## Objective
This lab demonstrates how to build and run a Java application using Gradle. You will clone a repository, run unit tests, generate a JAR file (artifact), run the application, and verify it works.

---

## Steps

### 1. Install Gradle

#### CentOS:
```bash
wget https://services.gradle.org/distributions/gradle-8.7-bin.zip -P /tmp

sudo unzip -d /opt/gradle /tmp/gradle-8.7-bin.zip

sudo tee /etc/profile.d/gradle.sh > /dev/null <<EOF
export GRADLE_HOME=/opt/gradle/gradle-8.7
export PATH=\$PATH:\$GRADLE_HOME/bin
EOF

sudo chmod +x /etc/profile.d/gradle.sh

source /etc/profile.d/gradle.sh

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

<img width="633" alt="Lab9_1" src="https://github.com/user-attachments/assets/9da1299b-082d-42c8-90dc-453f8086f10a" />



---

### 5. Build the Application

```bash
gradle build
```

This will generate the JAR file at:
```
build/libs/ivolve-app.jar
```
<img width="632" alt="Lab9_2" src="https://github.com/user-attachments/assets/af7fd367-db46-4e2f-b85f-11c02ed43d9e" />

---

### 6. Run the Application

```bash
java -jar build/libs/ivolve-app.jar
```
<img width="631" alt="Lab9_3" src="https://github.com/user-attachments/assets/f9ae5644-e24c-4145-8a57-fb83be4129d6" />

---

### 7. Verify the Application

You should see output in the terminal indicating that the application is working as expected.

---
