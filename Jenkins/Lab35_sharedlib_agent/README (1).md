
# Jenkins Shared Library – Lab 35

This shared library is used in **Lab 35: CI/CD Pipeline Implementation with Jenkins Agents and Shared Libraries**.

It supports a multi-stage Jenkins pipeline that performs testing, building, Docker image creation, image scanning, pushing to Docker Hub, and Kubernetes deployment.

---

## 📁 Folder Structure

```
jenkins_shared_library/
├── vars/
│   ├── runUnitTests.groovy
│   ├── buildApp.groovy
│   ├── buildImage.groovy
│   ├── scanImage.groovy
│   ├── pushImage.groovy
│   ├── removeImage.groovy
│   └── deployOnK8s.groovy
└── README.md
```

---

## 🧩 Library Functions

| Function Name      | Description                                   |
|--------------------|-----------------------------------------------|
| `runUnitTests()`   | Runs unit tests using Maven                   |
| `buildApp()`       | Builds the Java application using Maven       |
| `buildImage(img)`  | Builds a Docker image from the app source     |
| `scanImage(img)`   | (Optional) Simulates scanning the Docker image|
| `pushImage(img, user, pass)` | Pushes image to Docker Hub         |
| `removeImage(img)` | Removes image locally to save space           |
| `deployOnK8s(api, token, ns, img, file)` | Deploys to Kubernetes  |

---

## 🔗 Usage in Jenkinsfile

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent { label 'agent-vm-1' }

    environment {
        DOCKER_USERNAME = credentials('Docker-User')
        DOCKER_PASSWORD = credentials('Docker-Password')
        DOCKER_IMAGE = "${DOCKER_USERNAME}/jenkins-app-lab34:${BUILD_NUMBER}"
        K8S_API_SERVER = credentials('api-server')
        K8S_TOKEN = credentials('token')
        NAMESPACE = 'ivolve'
        DEPLOYMENT_FILE = 'Jenkins/Lab34_pipeline/k8s/deployment.yaml'
    }

    stages {
        stage('Run Tests') {
            steps {
                runUnitTests()
            }
        }
        stage('Build App') {
            steps {
                buildApp()
            }
        }
        stage('Build Image') {
            steps {
                buildImage(env.DOCKER_IMAGE)
            }
        }
        stage('Scan Image') {
            steps {
                scanImage(env.DOCKER_IMAGE)
            }
        }
        stage('Push Image') {
            steps {
                pushImage(env.DOCKER_IMAGE, env.DOCKER_USERNAME, env.DOCKER_PASSWORD)
            }
        }
        stage('Remove Image') {
            steps {
                removeImage(env.DOCKER_IMAGE)
            }
        }
        stage('Deploy to K8s') {
            steps {
                deployOnK8s(env.K8S_API_SERVER, env.K8S_TOKEN, env.NAMESPACE, env.DOCKER_IMAGE, env.DEPLOYMENT_FILE)
            }
        }
    }
}
```

---

## 🔐 Credentials Required

Ensure these Jenkins credentials are created:
- `Docker-User`
- `Docker-Password`
- `api-server`
- `token`

---

## 🖥 Agent

Pipeline uses a configured Jenkins agent labeled: `agent-vm-1`

---

## ✅ Status

✔️ Verified working with Jenkins + Docker + Kubernetes

---

## 📌 Author

- [@Orabi20](https://github.com/Orabi20)
