
# Lab 24: Node.js Application Deployment with ClusterIP Service

This lab demonstrates how to deploy a Node.js application on Kubernetes using:
- Custom Docker image
- ConfigMap & Secret for MySQL credentials
- Toleration
- PersistentVolume
- ClusterIP Service

---

## 📁 Prerequisites

- Minikube running
- Docker image `orabi20/phpmyadmin:v2` pushed to Docker Hub
- Kubernetes namespace: `ivolve`

---

## 🔧 Step 1: Create Namespace

```bash
kubectl create namespace ivolve
```

---

## 🔐 Step 2: Create Secret and ConfigMap

```yaml
# mysql-configmap-secret.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mysql-config
  namespace: ivolve
data:
  DB_HOST: mysql-0.mysql.ivolve.svc.cluster.local
  DB_USER: ivolveuser
---
apiVersion: v1
kind: Secret
metadata:
  name: mysql-secret
  namespace: ivolve
type: Opaque
data:
  DB_PASSWORD: aXZvbHZlcGFzcw==            # base64 for 'ivolvepass'
  MYSQL_ROOT_PASSWORD: cm9vdHBhc3M=        # base64 for 'rootpass'
```

Apply it:

```bash
kubectl apply -f mysql-configmap-secret.yaml
```

---

## 💾 Step 3: Create PersistentVolume and PersistentVolumeClaim

```yaml
# pv-pvc.yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: app-logs-pv
spec:
  capacity:
    storage: 1Gi
  accessModes:
    - ReadWriteOnce
  persistentVolumeReclaimPolicy: Retain
  hostPath:
    path: /mnt/app
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: app-logs-pvc
  namespace: ivolve
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

Apply it:

```bash
kubectl apply -f pv-pvc.yaml
```

---

## 🚀 Step 4: Deploy Node.js Application

```yaml
# deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nodejs-app
  namespace: ivolve
spec:
  replicas: 2
  selector:
    matchLabels:
      app: nodejs
  template:
    metadata:
      labels:
        app: nodejs
    spec:
      tolerations:
        - key: "workload"
          operator: "Equal"
          value: "app"
          effect: "NoSchedule"
      containers:
        - name: nodejs-container
          image: orabi20/phpmyadmin:v2
          ports:
            - containerPort: 3000
          env:
            - name: DB_HOST
              valueFrom:
                configMapKeyRef:
                  name: mysql-config
                  key: DB_HOST
            - name: DB_USER
              valueFrom:
                configMapKeyRef:
                  name: mysql-config
                  key: DB_USER
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-secret
                  key: DB_PASSWORD
          volumeMounts:
            - name: logs-storage
              mountPath: /usr/src/app/data
      volumes:
        - name: logs-storage
          persistentVolumeClaim:
            claimName: app-logs-pvc
```

Apply it:

```bash
kubectl apply -f deployment.yaml
```

---

## 📡 Step 5: Create ClusterIP Service

```yaml
# service.yaml
apiVersion: v1
kind: Service
metadata:
  name: nodejs-service
  namespace: ivolve
spec:
  selector:
    app: nodejs
  ports:
    - port: 3000
      targetPort: 3000
  type: ClusterIP
```

Apply it:

```bash
kubectl apply -f service.yaml
```

---

## 🧪 Step 6: Test via Port Forwarding

```bash
kubectl port-forward svc/nodejs-service 8080:3000 -n ivolve
```

Then open your browser and go to:

```
http://localhost:8080
```

---

## 🧹 Optional: Clean Up

```bash
kubectl delete ns ivolve
```

---

## 📝 Notes

- Make sure your Minikube has enough resources to schedule all pods.
- If you hit pod limit errors, increase your ResourceQuota or delete old pods.
- Log files will be stored (if enabled in the app) at `/mnt/app` on the Minikube host.

---

## ✅ Lab Complete
