# 🚀 Lab 37: GitOps Workflow with ArgoCD

## 📌 Objective
Automate application deployment using Jenkins, Docker, GitHub, and ArgoCD within a Kubernetes cluster by implementing a GitOps workflow.

---

## 🛠️ Prerequisites

- Kubernetes cluster (Minikube, kind, EKS, etc.)
- ArgoCD installed in the cluster
- Jenkins installed and accessible
- Docker installed and authenticated to Docker Hub
- GitHub repository access

---

## ⚙️ CI/CD Pipeline Steps

The Jenkins pipeline automates the following tasks:

### 1️⃣ Build Application

If it’s a Java Maven project:
```bash
mvn clean package
```

### 2️⃣ Build Docker Image

```bash
docker build -t <dockerhub-username>/jenkins-app:<tag> .
```

### 3️⃣ Push Docker Image to Docker Hub

```bash
docker login
docker push <dockerhub-username>/jenkins-app:<tag>
```

### 5️⃣ Update Deployment Manifest

Open `k8s/deployment.yaml` (or similar) and change the image tag:
```yaml
spec:
  containers:
    - name: jenkins-app
      image: <dockerhub-username>/jenkins-app:<new-tag>
```

### 6️⃣ Push Changes to GitHub

```bash
git add .
git commit -m "Update image to <new-tag>"
git push origin main
```

---

## 🤖 Jenkinsfile


```yaml
pipeline {
    agent any

    environment {
        DOCKER_USERNAME = credentials('Docker-User')
        DOCKER_PASSWORD = credentials('Docker-Password')
        DOCKER_IMAGE = "${DOCKER_USERNAME}/jenkins-app-lab37:${BUILD_NUMBER}"
        K8S_TOKEN = credentials('token')
        NAMESPACE = 'ivolve'
        DEPLOYMENT_FILE = 'Jenkins/Lab37_argocd/CD_repo/deployment.yaml'
        GIT_CREDENTIALS_ID = 'github'
    }

    stages {
        stage('Build Application') {
            steps {
              dir('Jenkins/Lab37_argocd/CI_repo'){  
                echo 'Building application...'
                sh 'mvn clean package'}
            }
        }

        stage('Build Docker Image') {
            steps {
              dir('Jenkins/Lab37_argocd/CI_repo'){
                echo 'Building Docker image...'
                sh 'docker build -t $DOCKER_IMAGE .
            }
          }      
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
              dir('Jenkins/Lab37_argocd/CI_repo'){
                echo 'Pushing Docker image to Docker Hub...'
                sh '''
                    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                    docker push $DOCKER_IMAGE
                '''
               }
            }      
        }

        stage('Clone ArgoCD Manifests Repo') {
            steps {
                 dir('Jenkins/Lab37_argocd/CD_repo') {
                    git branch: 'master',
                        credentialsId: GIT_CREDENTIALS_ID,
                        url: 'https://github.com/Orabi20/iVOLVE-OJT'
                 }
            }
        }

        stage('Update Deployment File with New Image') {
            steps {
                dir('Jenkins/Lab37_argocd/CD_repo') {
                    withCredentials([usernamePassword(credentialsId: GIT_CREDENTIALS_ID, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh '''
                            git config user.name "jenkins"
                            git config user.email "jenkins@example.com"

                            sed -i 's|image:.*|image: '"$DOCKER_IMAGE"'|' $DEPLOYMENT_FILE
                            git add $DEPLOYMENT_FILE
                            git commit -m "Update image to $DOCKER_IMAGE" || echo "No changes to commit"
                            git push https://$GIT_USERNAME:$GIT_PASSWORD@github.com/Orabi20/iVOLVE-OJT.git
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up workspace...'
            sh 'rm -rf Jenkins/Lab37_argocd/CD_repo || true'
        }
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
```

# Before Applying Change:

<img width="959" height="469" alt="37 1" src="https://github.com/user-attachments/assets/720c1fc6-e1b0-48b9-85ca-22252fc95269" />

<img width="959" height="473" alt="37 2" src="https://github.com/user-attachments/assets/12c6e973-97d1-4ca3-a048-6919f87b0ecf" />

# After Applying Change:

<img width="928" height="132" alt="37 4" src="https://github.com/user-attachments/assets/4da579a5-07d8-43ad-b6bb-c123f652490c" />

<img width="435" height="354" alt="37 5" src="https://github.com/user-attachments/assets/25e149d4-b109-410b-8a28-6eb6c38a0fcc" />

<img width="956" height="380" alt="37 6" src="https://github.com/user-attachments/assets/04f74284-2043-4ab5-811d-625e6252705d" />

<img width="959" height="472" alt="37 7" src="https://github.com/user-attachments/assets/3776e89a-b510-4250-9875-a6eccc39dc59" />

<img width="959" height="466" alt="37 8" src="https://github.com/user-attachments/assets/5d89a57b-6108-4732-84ed-480dd8212daa" />


---

## ✅ Validation

Check the deployment in the Kubernetes cluster:
```bash
kubectl get po -n ivolve
```
<img width="947" height="151" alt="image" src="https://github.com/user-attachments/assets/f4317578-486c-4c7c-ad22-c8a27530a9ac" />


---




