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

## 🔗 Clone Docker File Repository

```bash
git clone https://github.com/Ibrahim-Adel15/Jenkins_App.git
cd Jenkins_App
```

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

### 4️⃣ Delete Local Image (Optional Cleanup)

```bash
docker rmi <dockerhub-username>/jenkins-app:<tag>
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

## 🤖 ArgoCD GitOps Deployment

ArgoCD monitors your GitHub repo. When it detects updates in the Kubernetes manifests:

```yaml
# Sample ArgoCD Application manifest
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: jenkins-app
  namespace: argocd
spec:
  destination:
    name: ''
    namespace: default
    server: 'https://kubernetes.default.svc'
  source:
    path: k8s
    repoURL: 'https://github.com/<your-username>/Jenkins_App.git'
    targetRevision: HEAD
  project: default
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
```

Once synced, ArgoCD applies the updated manifests into the cluster.

---

## ✅ Validation

Check the deployment in the Kubernetes cluster:
```bash
kubectl get pods
kubectl get svc
```

You can also use ArgoCD UI to verify the application status and sync state.

---

## 🔁 GitOps Workflow Diagram

```text
Developer -> Push code to GitHub
GitHub -> Triggers Jenkins via webhook
Jenkins -> Builds app, Dockerizes it, pushes image, updates manifest
GitHub -> Stores updated deployment.yaml
ArgoCD -> Detects update and syncs with Kubernetes
Kubernetes -> New version of app deployed
```

---

## 🧾 References

- Docker Hub: [https://hub.docker.com](https://hub.docker.com)
- ArgoCD Docs: [https://argo-cd.readthedocs.io](https://argo-cd.readthedocs.io)
- GitHub Repo: [https://github.com/Ibrahim-Adel15/Jenkins_App.git](https://github.com/Ibrahim-Adel15/Jenkins_App.git)

---

© iVolve - DevOps Training Labs
