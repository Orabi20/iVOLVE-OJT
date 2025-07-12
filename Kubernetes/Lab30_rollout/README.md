# Lab 30: Deployment Update and Roll Back

This lab demonstrates how to update a Kubernetes deployment with a new Docker image and perform a rollback if needed.

---

## 🛠️ Steps

### 1. Modify the Application

- Open `/frontend/index.html`.
- Replace the word `Egypt` with `Cairo`.

---

### 2. Build Docker Image using Docker Compose

```bash
docker compose up --build
```

> Ensure `docker-compose.yml` includes a build context and a tagged image.

---

### 3. Push the New Docker Image to Docker Hub

```bash
docker push yourusername/yourimage:V2
```
<img width="424" height="336" alt="30 1" src="https://github.com/user-attachments/assets/459bddc8-a052-4905-8872-625f5b271e2f" />

---

### 4. Update Kubernetes Deployment

- Edit your `deployment.yml` file:
  ```yaml
  containers:
    - name: nodejs-app
      image: yourusername/yourimage:V2
  ```
- Apply the update:
  ```bash
  kubectl apply -f deployment.yml
  kubectl rollout  deployment nodejs-app -n ivolve

  ```
<img width="942" height="42" alt="30 2" src="https://github.com/user-attachments/assets/409aff1e-9291-4d30-b31c-05e5305e6e1f" />

<img width="950" height="42" alt="30 3" src="https://github.com/user-attachments/assets/66891454-c2e2-4520-b1d6-74ecd23fee12" />

<img width="953" height="155" alt="30 4" src="https://github.com/user-attachments/assets/a49d8344-e9cc-4079-93b9-9388f26f1653" />



---

### 5. Verify the Update

- Access the application in a browser or via `kubectl port-forward` and ensure it displays **Cairo**.
 ```bash
 kubectl port-forward svc/nodejs-service 8080:3000 -n ivolve

 ```

<img width="959" height="430" alt="30 5" src="https://github.com/user-attachments/assets/b958ad68-3f13-49fb-80cf-0c9617107450" />

---

### 6. Check Rollout History

```bash
kubectl rollout history deployment nodejs-app -n ivolve
```
<img width="950" height="290" alt="30 6" src="https://github.com/user-attachments/assets/9da41192-572d-4e4d-b1b7-cfbc756fc414" />

---

### 7. Rollback to Previous Version

```bash
kubectl rollout undo deployment nodejs-app -n ivolve
```

---

### 8. Monitor Rollback

```bash
kubectl get pods -w -n ivolve
kubectl rollout status deployment nodejs-app -n ivolve
```

---

### 9. Re-Verify Rollback

- Ensure the app displays **Egypt** again.

<img width="955" height="434" alt="30 8" src="https://github.com/user-attachments/assets/7e41797d-1526-4084-a3c3-2133f49c578f" />

---

## 📁 Files Involved

- `frontend/index.html` — App content
- `Dockerfile` — Docker image instructions
- `docker-compose.yml` — For building image
- `deployment.yaml` — Kubernetes deployment
- `service.yaml` — Kubernetes service

---


---

## ✅ Notes

- Make sure Docker is logged in before pushing.
- Use meaningful image tags to simplify rollbacks.
