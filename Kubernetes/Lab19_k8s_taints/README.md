# Lab 19: Node Isolation Using Taints in Kubernetes

## 🧪 Objective
Isolate Kubernetes workloads by tainting nodes to restrict pod scheduling unless explicitly tolerated.

---

## ⚙️ Lab Setup

- Kubernetes cluster with **3 nodes**
  - Tools: Minikube (multi-node), kubeadm, EKS, etc.

---

## 🛠️ Steps

### 1. Start a 3-node Kubernetes Cluster
Example using Minikube:
```bash
minikube start --nodes 3 -p taint-lab
kubectl get nodes
```

---

### 2. Taint Nodes

#### 🔹 Taint Node 1 (Master Node)
```bash
kubectl taint nodes <node-name-1> workload=master:NoSchedule
```

#### 🔹 Taint Node 2 (App Node)
```bash
kubectl taint nodes <node-name-2> workload=app:NoSchedule
```

#### 🔹 Taint Node 3 (Database Node)
```bash
kubectl taint nodes <node-name-3> workload=database:NoSchedule
```

---

### 3. Verify Taints
```bash
kubectl describe nodes | grep -i taint
```

---

## 📘 Taint Effect Types

| Effect Type        | Description                                          |
|--------------------|------------------------------------------------------|
| `NoSchedule`       | Prevents scheduling pods that don't tolerate taint   |
| `PreferNoSchedule` | Tries to avoid scheduling, but may allow it          |
| `NoExecute`        | Evicts existing pods and prevents new ones           |

---

## ✅ Example Taint Command

```bash
kubectl taint nodes node1 workload=master:NoSchedule
```

## ✅ Example Toleration in Pod

```yaml
tolerations:
- key: "workload"
  operator: "Equal"
  value: "master"
  effect: "NoSchedule"
```

---

## 📌 Notes

- Replace `<node-name-1>` etc. with actual node names from `kubectl get nodes`.
- Use `tolerations` in pod YAMLs to allow pods to be scheduled on tainted nodes.
