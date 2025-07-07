
# Lab 22: Persistent Storage Setup for Application Logging

This lab demonstrates how to configure persistent storage for application logging using a Persistent Volume (PV) and a Persistent Volume Claim (PVC) in a Minikube environment.

---

## 📦 Persistent Volume (PV) Configuration

- **Name**: `app-logs-pv`
- **Storage Size**: 1Gi
- **Storage Type**: `hostPath`
- **Host Path**: `/mnt/app-logs` (on the Minikube node)
- **Access Mode**: `ReadWriteMany`
- **Reclaim Policy**: `Retain`

> 📌 Note: The directory `/mnt/app-logs` must be manually created inside the Minikube node with `777` permissions.

### Create Directory on Minikube Node

```bash
minikube ssh
sudo mkdir -p /mnt/app-logs
sudo chmod 777 /mnt/app-logs
exit
```

---

## 📄 Persistent Volume Claim (PVC) Configuration

- **Name**: `app-logs-pvc`
- **Requested Storage**: 1Gi
- **Access Mode**: `ReadWriteMany`

---

## 📁 YAML File: `lab22-pv-pvc.yaml`

```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: app-logs-pv
spec:
  capacity:
    storage: 1Gi
  accessModes:
    - ReadWriteMany
  persistentVolumeReclaimPolicy: Retain
  hostPath:
    path: /mnt/app-logs
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: app-logs-pvc
spec:
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 1Gi
```

---

## ✅ Apply the Configuration

```bash
kubectl apply -f lab22-pv-pvc.yaml
```

<img width="955" alt="22 1" src="https://github.com/user-attachments/assets/5b45885a-b88c-449c-b6b3-fb81e50c5fef" />

## 🔍 Verify

```bash
kubectl get pv
kubectl get pvc
```

<img width="959" alt="22 2" src="https://github.com/user-attachments/assets/e63655b5-0c9d-451d-b085-72901f1c80ca" />

<img width="954" alt="22 3" src="https://github.com/user-attachments/assets/f490fb9a-b147-4b03-9215-0453a2b4f7f6" />


---

