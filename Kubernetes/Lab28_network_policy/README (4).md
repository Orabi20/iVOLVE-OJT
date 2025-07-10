
# Lab 28: Control Pod-to-Pod Traffic via Network Policy

## 🎯 Objective
Allow only application pods to access MySQL pods on port **3306**. Deny all other pod-to-pod traffic.

---

## 🛠️ Steps

### Step 1: Create Namespace (if not already created)
```bash
kubectl create namespace ivolve
```

---

### Step 2: Deploy MySQL and Application Pods

#### 2.1. MySQL Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  namespace: ivolve
spec:
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
        - name: mysql
          image: mysql:5.7
          env:
            - name: MYSQL_ROOT_PASSWORD
              value: rootpass
          ports:
            - containerPort: 3306
```

#### 2.2. Node.js App Deployment
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
      containers:
        - name: app
          image: node:18
          command: ["sleep", "3600"]
```

> Apply both:
```bash
kubectl apply -f mysql-deployment.yaml
kubectl apply -f app-deployment.yaml
```

---

### Step 3: Apply NetworkPolicy
```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-app-to-mysql
  namespace: ivolve
spec:
  podSelector:
    matchLabels:
      app: mysql
  policyTypes:
    - Ingress
  ingress:
    - from:
        - podSelector:
            matchLabels:
              app: nodejs
      ports:
        - protocol: TCP
          port: 3306
```

> Apply it:
```bash
kubectl apply -f network-policy.yaml
```

---

### Step 4: Verify the Policy

#### 4.1. From app pod (should succeed)
```bash
kubectl exec -it -n ivolve deploy/nodejs-app -- bash
apt update && apt install -y mysql-client
mysql -h mysql -uroot -p
```

#### 4.2. From other pods (should fail)
Since you have no other pods now, we assume NetworkPolicy is blocking all pods **not labeled `app=nodejs`** by default.

---

## 🔒 Note on Ports
NetworkPolicy only allows traffic to destination port `3306`.  
If a request from the Node.js app tries to reach MySQL on **another port (e.g., 2000)**, it will be **blocked**.

Also, MySQL only listens on port `3306`, so connections to other ports will fail regardless.

---

## ✅ Summary
| Source Pod Label | Destination Port | Allowed |
|------------------|------------------|---------|
| app=nodejs       | 3306             | ✅ Yes  |
| app=nodejs       | 2000             | ❌ No   |
| other pods       | 3306             | ❌ No   |
| other pods       | 2000             | ❌ No   |

---
