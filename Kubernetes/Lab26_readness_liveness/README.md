# Lab 26: Health Monitoring of Application Pods

This lab demonstrates how to configure **readiness** and **liveness probes** in a Kubernetes deployment to monitor the health of application containers.

---

## 🛠️ Objectives

- Configure a **readiness probe** to check when the application is ready to receive traffic.
- Configure a **liveness probe** to detect if the application is still running.
- Forward traffic to the application locally for testing.

---

## 🧱 Application Details

- **Namespace**: `ivolve`
- **App Port**: `3000` (inside the container)
- **Accessible Path**: `/health`

---

## 📦 Deployment Configuration

### ✅ Readiness and Liveness Probes

Probes are defined to monitor `/health` on port `3000`:

```yaml
readinessProbe:
  httpGet:
    path: /ready
    port: 3000
  initialDelaySeconds: 5
  periodSeconds: 10
  timeoutSeconds: 1
  failureThreshold: 3
  successThreshold: 1

livenessProbe:
  httpGet:
    path: /health
    port: 3000
  initialDelaySeconds: 10
  periodSeconds: 20
  timeoutSeconds: 1
  failureThreshold: 3
  successThreshold: 1
```
<img width="766" alt="image" src="https://github.com/user-attachments/assets/6ff1c582-6146-4618-81d0-92c96224b5ae" />


These probes help Kubernetes decide:
- When to start routing traffic to the pod (readiness).
- When to restart the container if it's unresponsive (liveness).

---

## 🌐 Service Configuration

The service exposes port `80` and forwards it to container port `3000`:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: nodejs-service
  namespace: ivolve
spec:
  selector:
    app: nodejs
  ports:
    - protocol: TCP
      port: 3000
      targetPort: 3000
  type: ClusterIP
```

---

## 🚀 Port Forwarding for Local Access

To access the app from your browser on port `8080`:

```bash
kubectl port-forward svc/nodejs-service 8080:3000 -n ivolve
```

Then open:

```
http://localhost:8080/
```

---

## ✅ Success Criteria

- Pod status is `Running` and `Ready`.
- Probes do not fail or trigger restarts.
- App loads successfully at `http://localhost:8080/`.

---
