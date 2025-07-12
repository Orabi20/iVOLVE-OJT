# Lab 31: Securing Kubernetes with RBAC and Service Accounts

## Objective
Create a ServiceAccount `jenkins-sa` in the `ivolve` namespace and limit its permissions to only read pods. Then generate a `kubeconfig` file for external tools like Jenkins to access the cluster using this account.

---

## 1. Create ServiceAccount

```bash
kubectl create namespace ivolve
kubectl create serviceaccount jenkins-sa -n ivolve
```
<img width="950" height="42" alt="31 1" src="https://github.com/user-attachments/assets/cfcd19be-8b72-4c88-b80e-9e2eec0238f9" />

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
<img width="943" height="43" alt="31 3" src="https://github.com/user-attachments/assets/147c8907-f598-48e5-bdf2-1f1ad9de7aec" />

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
<img width="954" height="43" alt="31 4" src="https://github.com/user-attachments/assets/79e57215-3730-4f48-a976-653a0520e2f8" />

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

<img width="956" height="106" alt="31 5" src="https://github.com/user-attachments/assets/7d11bd6e-6db1-43bc-bed0-cff90de64048" />


<img width="945" height="62" alt="31 6" src="https://github.com/user-attachments/assets/f2b8a332-852c-4c5c-ab6a-64740afb6b49" />

---


---

## ✅ Summary

| Component     | Purpose                          |
|---------------|----------------------------------|
| `jenkins-sa`  | The ServiceAccount for Jenkins   |
| `pod-reader`  | Role with read-only access       |
| `RoleBinding` | Binds the Role to the SA         |
| `kubeconfig`  | Used to authenticate externally  |
