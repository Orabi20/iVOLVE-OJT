# Lab 31: Securing Kubernetes with RBAC and Service Accounts

## Objective
Create a ServiceAccount `jenkins-sa` in the `ivolve` namespace and limit its permissions to only read pods. Then generate a `kubeconfig` file for external tools like Jenkins to access the cluster using this account.

---

## 1. Create ServiceAccount

```bash
kubectl create namespace ivolve
kubectl create serviceaccount jenkins-sa -n ivolve
```

---

## 2. Create Role for Pod Read Access

Save as `pod-reader-role.yaml`:

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: pod-reader
  namespace: ivolve
rules:
- apiGroups: [""]
  resources: ["pods"]
  verbs: ["get", "list"]
```

Apply it:

```bash
kubectl apply -f pod-reader-role.yaml
```

---

## 3. Bind Role to ServiceAccount

Save as `pod-reader-rolebinding.yaml`:

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: pod-reader-binding
  namespace: ivolve
subjects:
- kind: ServiceAccount
  name: jenkins-sa
  namespace: ivolve
roleRef:
  kind: Role
  name: pod-reader
  apiGroup: rbac.authorization.k8s.io
```

Apply it:

```bash
kubectl apply -f pod-reader-rolebinding.yaml
```

---

## 4. Generate Token for ServiceAccount (Kubernetes ≥1.24)

```bash
kubectl create token jenkins-sa -n ivolve
```

Copy the token value for use in the next step.

---

## 5. Generate kubeconfig for `jenkins-sa`

```bash
SA_NAME=jenkins-sa
NAMESPACE=ivolve
TOKEN=$(kubectl create token $SA_NAME -n $NAMESPACE)
CLUSTER_NAME=$(kubectl config view --minify -o jsonpath='{.clusters[0].name}')
CLUSTER_SERVER=$(kubectl config view --minify -o jsonpath='{.clusters[0].cluster.server}')
CA_CERT=$(kubectl config view --raw --minify --flatten -o jsonpath='{.clusters[0].cluster.certificate-authority-data}')

cat <<EOF > kubeconfig-jenkins-sa
apiVersion: v1
kind: Config
clusters:
- name: $CLUSTER_NAME
  cluster:
    server: $CLUSTER_SERVER
    certificate-authority-data: $CA_CERT
contexts:
- name: jenkins-context
  context:
    cluster: $CLUSTER_NAME
    user: jenkins-user
    namespace: $NAMESPACE
current-context: jenkins-context
users:
- name: jenkins-user
  user:
    token: $TOKEN
EOF
```

---

## 6. Test the kubeconfig

```bash
KUBECONFIG=kubeconfig-jenkins-sa kubectl get pods -n ivolve
```

You should see a list of pods (if any), but other operations like `create`, `delete`, or accessing other resource types will be denied.

---

## 7. Verify Access

```bash
KUBECONFIG=kubeconfig-jenkins-sa kubectl auth can-i create pods -n ivolve
```

Expected Output:
```
no
```

---

## ✅ Summary

| Component     | Purpose                          |
|---------------|----------------------------------|
| `jenkins-sa`  | The ServiceAccount for Jenkins   |
| `pod-reader`  | Role with read-only access       |
| `RoleBinding` | Binds the Role to the SA         |
| `kubeconfig`  | Used to authenticate externally  |