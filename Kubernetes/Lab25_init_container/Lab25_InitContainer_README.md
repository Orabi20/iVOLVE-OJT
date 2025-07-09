# Lab 25: Kubernetes Init Container for Pre-Deployment Database Setup

## 🎯 Objective

Set up an `initContainer` in a Kubernetes deployment to:

- Create the `ivolve` MySQL database
- Create a new MySQL user with full access to that database
- Ensure this runs **before** the main application container starts

---

## 🛠 Prerequisites

- Kubernetes cluster is running (e.g., Minikube)
- MySQL database deployed and accessible via a Service (e.g., `mysql`)
- Namespace `ivolve` is created

---

## 📁 Files Needed

### 1. `mysql-secret.yaml`
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mysql-secret
  namespace: ivolve
type: Opaque
data:
  MYSQL_ROOT_PASSWORD: <base64-root-password>
  DB_PASSWORD: <base64-db-user-password>
```

### 2. `mysql-config.yaml`
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mysql-config
  namespace: ivolve
data:
  DB_HOST: mysql
  DB_USER: ivolve_user
```

### 3. `deployment.yaml`
Includes the init container and the main container.

See sample snippet:

```yaml
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
        mysql -h $DB_HOST -u root -p"${MYSQL_ROOT_PASSWORD}" -e "
          CREATE DATABASE IF NOT EXISTS ivolve;
          CREATE USER IF NOT EXISTS '${DB_USER}'@'%' IDENTIFIED BY '${DB_PASSWORD}';
          GRANT ALL PRIVILEGES ON ivolve.* TO '${DB_USER}'@'%';
          FLUSH PRIVILEGES;";
        echo "Init container completed."
```

---

## 🚀 Apply the Resources

```bash
kubectl apply -f mysql-secret.yaml
kubectl apply -f mysql-config.yaml
kubectl apply -f deployment.yaml
```

---

## 🧪 Verify

### 1. Check pod status:
```bash
kubectl get pods -n ivolve
```

### 2. View init container logs:
```bash
kubectl logs <pod-name> -c init-mysql-setup -n ivolve
```

Should show:
```
Creating database and user...
Init container completed.
```

### 3. Connect to MySQL to verify:
```bash
kubectl port-forward svc/mysql 3306:3306 -n ivolve
mysql -h 127.0.0.1 -P 3306 -u ivolve_user -p
```

Then run:
```sql
SHOW DATABASES;
USE ivolve;
```

---

## ✅ Outcome

- Init container runs once per pod creation
- Database `ivolve` and user `ivolve_user` created
- Main application starts only **after** DB setup is complete