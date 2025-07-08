
# Lab 23: MySQL StatefulSet Deployment with Headless Service (Namespace: ivolve)

This lab demonstrates how to deploy a MySQL database using a Kubernetes StatefulSet and a Headless Service in the `ivolve` namespace.

---

## 📁 Files Included

- `mysql-secret.yaml` — Secret containing the MySQL root password.
- `mysql-headless-service.yaml` — Headless Service to expose StatefulSet pods.
- `mysql-statefulset.yaml` — StatefulSet that runs MySQL with persistent volumes and toleration.

---

## ⚙️ Step-by-Step Instructions

### 1. ✅ Namespace
Ensure the namespace `ivolve` exists:

```bash
kubectl create namespace ivolve
```

> If already created, skip this step.

---

### 2. 🔐 Create MySQL Secret

**mysql-secret.yaml**

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysql-secrets
  namespace: ivolve
type: Opaque
stringData:
  MYSQL_ROOT_PASSWORD: my-secret-password
```

Apply:

```bash
kubectl apply -f mysql-secret.yaml
```

---

### 3. 🌐 Create Headless Service

**mysql-headless-service.yaml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: mysql
  namespace: ivolve
spec:
  clusterIP: None
  selector:
    app: mysql
  ports:
  - port: 3306
    targetPort: 3306
```

Apply:

```bash
kubectl apply -f mysql-headless-service.yaml
```

---

### 4. 📦 Deploy StatefulSet

**mysql-statefulset.yaml**

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: mysql
  namespace: ivolve
spec:
  serviceName: mysql
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      tolerations:
      - key: "workload"
        operator: "Equal"
        value: "database"
        effect: "NoSchedule"
      containers:
      - name: mysql
        image: mysql:5.7
        ports:
        - containerPort: 3306
        env:
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-secrets
              key: MYSQL_ROOT_PASSWORD
        volumeMounts:
        - name: mysql-storage
          mountPath: /var/lib/mysql
  volumeClaimTemplates:
  - metadata:
      name: mysql-storage
    spec:
      accessModes: ["ReadWriteOnce"]
      resources:
        requests:
          storage: 1Gi
```

Apply:

```bash
kubectl apply -f mysql-statefulset.yaml
```

---

### 5. ✅ Verify Everything

```bash
kubectl get pods -n ivolve
kubectl get svc -n ivolve
kubectl get pvc -n ivolve
kubectl get endpoints -n ivolve
```

---

### 6. 🧪 Connect to MySQL

```bash
kubectl exec -it -n ivolve mysql-0 -- mysql -u root -p
```

Then enter `my-secret-password`

List databases:

```sql
SHOW DATABASES;
```

---

### 7. 🔍 Test DNS (Headless)

```bash
kubectl exec -it -n ivolve mysql-0 -- nslookup mysql
```

Expected DNS:
```
mysql-0.mysql.ivolve.svc.cluster.local
```

---

## ✅ Summary

- **StatefulSet** gives stable pod names and persistent storage.
- **Headless Service** gives pod-level DNS discovery.
- **Secret** protects sensitive credentials.
