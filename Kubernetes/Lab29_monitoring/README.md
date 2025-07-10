# Lab29: Kubernetes Monitoring with Prometheus and Grafana

This guide walks through installing **Prometheus** and **Grafana** in a Kubernetes cluster using Helm, handling node taints with tolerations, and visualizing metrics.

---

## 🚀 Prerequisites

- Kubernetes Cluster (minikube or any multi-node cluster)
- `kubectl` configured
- `helm` installed

---

## 📁 Project Structure

```
Lab29_monitoring/
├── deamonset.yml
├── values.yml
└── grafan.yml
```

---
## Deploy Node Exporter as DaemonSet
Node Exporter is already included in the Prometheus Helm chart, but here's how you'd manually apply it as a DaemonSet if needed:

```yaml
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: node-exporter
  namespace: monitoring
  labels:
    app: node-exporter
spec:
  selector:
    matchLabels:
      app: node-exporter
  template:
    metadata:
      labels:
        app: node-exporter
    spec:
      containers:
      - name: node-exporter
        image: quay.io/prometheus/node-exporter:latest
        ports:
        - containerPort: 9100
          name: metrics
        resources:
          limits:
            memory: 200Mi
            cpu: 100m
          requests:
            memory: 100Mi
            cpu: 100m
        volumeMounts:
        - name: proc
          mountPath: /host/proc
          readOnly: true
        - name: sys
          mountPath: /host/sys
          readOnly: true
        - name: root
          mountPath: /rootfs
          readOnly: true
      volumes:
      - name: proc
        hostPath:
          path: /proc
      - name: sys
        hostPath:
          path: /sys
      - name: root
        hostPath:
          path: /
```


```bash
k apply -f deamonset.yml
```
<img width="947" height="125" alt="29 1" src="https://github.com/user-attachments/assets/baaf4127-6866-4cc5-8ae6-e96002a6902f" />

<img width="958" height="440" alt="29 2" src="https://github.com/user-attachments/assets/01545b8b-9d15-4f5d-aea5-f238431a3501" />

---
## 1️⃣ Add Helm Repositories

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
```

---

## 2️⃣ Create Monitoring Namespace

```bash
kubectl create namespace monitoring
```

---

## 3️⃣ Deploy Prometheus

Create a file called `values.yml` with the following content:

```yaml
alertmanager:
  tolerations:
    - key: "workload"
      operator: "Exists"

kubeStateMetrics:
  tolerations:
    - key: "workload"
      operator: "Exists"

nodeExporter:
  tolerations:
    - key: "workload"
      operator: "Exists"

pushgateway:
  tolerations:
    - key: "workload"
      operator: "Exists"

server:
  tolerations:
    - key: "workload"
      operator: "Exists"
  persistentVolume:
    enabled: false
  extraVolumes:
    - name: data-volume
      emptyDir: {}
  extraVolumeMounts:
    - name: data-volume
      mountPath: /prometheus
  args:
    - "--storage.tsdb.retention.time=15d"
    - "--config.file=/etc/config/prometheus.yml"
    - "--storage.tsdb.path=/prometheus"
    - "--web.console.libraries=/etc/prometheus/console_libraries"
    - "--web.console.templates=/etc/prometheus/consoles"
    - "--web.enable-lifecycle"
  securityContext:
    runAsUser: 65534
    runAsGroup: 65534
    fsGroup: 65534
```

Then install Prometheus:

```bash
helm install prometheus prometheus-community/prometheus   -n monitoring   -f values.yml
```
<img width="948" height="229" alt="29 6" src="https://github.com/user-attachments/assets/1848d0ef-aa2e-48d8-9ac5-d045ffe21601" />


<img width="950" height="159" alt="29 5" src="https://github.com/user-attachments/assets/19a278fd-a42e-49c7-8e8a-4bc6e0fbe0ba" />



---

## 4️⃣ Deploy Grafana

Create a file called `grafan.yml` with the following content:

```yaml
tolerations:
  - key: "workload"
    operator: "Exists"

adminUser: admin
adminPassword: admin
persistence:
  enabled: false
service:
  type: ClusterIP
```

Install Grafana:

```bash
helm install grafana grafana/grafana   -n monitoring   -f grafana-values.yaml
```

<img width="948" height="229" alt="29 6" src="https://github.com/user-attachments/assets/efc1e1a9-b9ba-46cf-bac8-ca5de9b21095" />


<img width="950" height="159" alt="29 5" src="https://github.com/user-attachments/assets/0fb5008d-7f6a-48c1-a8bb-2bb141e2f641" />


---

## 5️⃣ Access Grafana

Forward port 3000:

```bash
kubectl port-forward -n monitoring svc/grafana 3000:80
```

Open your browser:  
👉 http://localhost:3000  
**Username:** `admin`  
**Password:** `admin`

<img width="959" height="424" alt="29 3" src="https://github.com/user-attachments/assets/20f88a82-304d-4396-820e-ca1b101f71cc" />

---

## 6️⃣ Verify Prometheus Targets

Forward Prometheus:

```bash
kubectl port-forward -n monitoring svc/prometheus-server 9090:80
```

Visit:  
👉 http://localhost:9090  
Check **Status > Targets** — All targets should be `UP`.

<img width="959" height="434" alt="29 4" src="https://github.com/user-attachments/assets/3fa55003-d594-4708-b1f3-97d64168eaef" />
---

## 7️⃣ Create Grafana Dashboard

1. Go to **Grafana > Dashboards > New > Add Query**
2. Select **Prometheus** data source
3. Try sample queries:

- Node health: `up`
- CPU usage: `rate(node_cpu_seconds_total{mode!="idle"}[1m])`
- Memory usage: `node_memory_MemAvailable_bytes`

<img width="959" height="424" alt="29 3" src="https://github.com/user-attachments/assets/20f88a82-304d-4396-820e-ca1b101f71cc" />
---

## ✅ Troubleshooting

| Issue | Fix |
|-------|-----|
| Grafana or Prometheus `Pending` | Add tolerations for `workload` taints |
| Prometheus pod crashes with `/data` error | Use `emptyDir` for storage and mount at `/prometheus` |
| Grafana shows "No data" | Check Prometheus `Targets` tab, make sure `up` query returns results |
| Cannot pull charts | Check network/firewall or use offline tarballs |

---

## 📌 Notes

- Data is not persisted because persistence is disabled (`emptyDir`). Use PVCs in production.
- You can expose Grafana using `NodePort` or Ingress if needed.



