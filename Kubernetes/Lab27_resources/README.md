
# Lab 27: Pod Resource Management with CPU and Memory Requests and Limits

This lab demonstrates how to manage Kubernetes pod resources by applying CPU and memory requests and limits to a Node.js deployment.

---

## ✅ Objectives

- Update the existing Node.js Deployment to include resource requests and limits.
- Verify resource constraints using `kubectl describe`.

---

## 🛠️ Deployment Configuration

Update your `nodejs-app` Deployment with the following:

```yaml
resources:
  requests:
    cpu: "1"        # Minimum guaranteed CPU
    memory: "1Gi"   # Minimum guaranteed memory
  limits:
    cpu: "2"        # Maximum CPU the pod can use
    memory: "2Gi"   # Maximum memory the pod can use
```

Apply this inside the container spec for `nodejs-container`.

---

## 📂 Full Deployment File (Example)

Make sure your deployment YAML includes the `resources:` section like this (inside `containers:`):

```yaml
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

      initContainers:
        - name: init-mysql-setup
          image: mysql:5.7
          env:
            - name: DB_HOST
              valueFrom:
                configMapKeyRef:
                  name: mysql-config
                  key: DB_HOST 
            - name: MYSQL_ROOT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-secret
                  key: MYSQL_ROOT_PASSWORD
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
          command: ["sh", "-c"]
          args:
            - |
              echo "Waiting for MySQL to be ready..."
              until mysqladmin ping -h $DB_HOST --silent; do
                echo "Waiting for MySQL..."
                sleep 2
              done
              echo "Creating database and user..."
              mysql -h $DB_HOST -u root -p${MYSQL_ROOT_PASSWORD} -e "
                CREATE DATABASE IF NOT EXISTS ivolve;
                CREATE USER IF NOT EXISTS '${DB_USER}'@'%' IDENTIFIED BY '${DB_PASSWORD}';
                GRANT ALL PRIVILEGES ON ivolve.* TO '${DB_USER}'@'%';
                FLUSH PRIVILEGES;";
              echo "Init container completed."

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

          resources:
            requests:
              cpu: "1"
              memory: "1Gi"
            limits:
              cpu: "2"
              memory: "2Gi"

          readinessProbe:
            httpGet:
              path: /ready
              port: 3000
          livenessProbe:
            httpGet:
              path: /health
              port: 3000

      volumes:
        - name: logs-storage
          persistentVolumeClaim:
            claimName: app-logs-pvc
```

---

## 🔍 Verification Steps

### 1. Apply the Deployment

```bash
kubectl apply -f nodejs-deployment.yaml
```

### 2. Describe the Pod

```bash
kubectl describe pod -n ivolve
```





