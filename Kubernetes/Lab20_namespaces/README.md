# Lab 20: Namespace Management and Resource Quota Enforcement

## 🎯 Objective

- Create a Kubernetes namespace called `ivolve`.
- Enforce a resource quota that limits the number of pods in this namespace to **2**.

---

## 📁 Resources

All configurations are defined in a single YAML file: `lab20-namespace-quota.yaml`.

---

## 📦 File: `lab20-namespace-quota.yaml`

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: ivolve
---
apiVersion: v1
kind: ResourceQuota
metadata:
  name: pod-limit
  namespace: ivolve
spec:
  hard:
    pods: "2"
```

---

## 🛠️ Apply Configuration

```bash
kubectl apply -f lab20-namespace-quota.yaml
```

---

## ✅ Verify

### Check namespace:
```bash
kubectl get namespaces
```

### Check resource quota in the namespace:
```bash
kubectl get resourcequota -n ivolve
kubectl describe resourcequota pod-limit -n ivolve
```

---

## 🔍 Test the Quota

Try creating more than 2 pods inside the `ivolve` namespace. The third pod should be **blocked** with a message indicating that the resource quota has been exceeded.

---

## 🧹 Cleanup (Optional)

```bash
kubectl delete namespace ivolve
```

---

## 📌 Notes

- Resource quotas help control resource consumption in multi-tenant environments.
- This example enforces a **hard limit** on the number of pods.
