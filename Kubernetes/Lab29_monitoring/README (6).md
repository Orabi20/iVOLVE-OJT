
# Kubernetes Monitoring with Prometheus and Grafana

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
├── prometheus-values.yaml
└── grafana-values.yaml
```

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

Create a file called `prometheus-values.yaml` with the following content:

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
helm install prometheus prometheus-community/prometheus   -n monitoring   -f prometheus-values.yaml
```

---

## 4️⃣ Deploy Grafana

Create a file called `grafana-values.yaml` with the following content:

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

---

## 6️⃣ Verify Prometheus Targets

Forward Prometheus:

```bash
kubectl port-forward -n monitoring svc/prometheus-server 9090:80
```

Visit:  
👉 http://localhost:9090  
Check **Status > Targets** — All targets should be `UP`.

---

## 7️⃣ Create Grafana Dashboard

1. Go to **Grafana > Dashboards > New > Add Query**
2. Select **Prometheus** data source
3. Try sample queries:

- Node health: `up`
- CPU usage: `rate(node_cpu_seconds_total{mode!="idle"}[1m])`
- Memory usage: `node_memory_MemAvailable_bytes`

---

## ✅ Troubleshooting

| Issue | Fix |
|-------|-----|
| Grafana or Prometheus `Pending` | Add tolerations for `workload` taints |
| Prometheus pod crashes with `/data` error | Use `emptyDir` for storage and mount at `/prometheus` |
| Grafana shows "No data" | Check Prometheus `Targets` tab, make sure `up` query returns results |
| Cannot pull charts | Check network/firewall or use offline tarballs |

---

## 🧼 Cleanup

To remove everything:

```bash
helm uninstall prometheus -n monitoring
helm uninstall grafana -n monitoring
kubectl delete namespace monitoring
```

---

## 📌 Notes

- Data is not persisted because persistence is disabled (`emptyDir`). Use PVCs in production.
- You can expose Grafana using `NodePort` or Ingress if needed.


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
