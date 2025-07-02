# Lab 15: Custom Docker Network for Microservices

## 🔗 Clone the Repository
```bash
git clone https://github.com/Ibrahim-Adel15/Docker5.git
cd Docker5
```

---

## 🐳 Dockerfile for Frontend

Path: `frontend/Dockerfile`

```dockerfile
FROM python:3.9

WORKDIR /app

COPY requirements.txt .

RUN pip install -r requirements.txt

COPY . .

EXPOSE 5000

CMD ["python", "app.py"]
```

---

## 🐳 Dockerfile for Backend

Path: `backend/Dockerfile`

```dockerfile
FROM python:3.9

WORKDIR /app

RUN pip install flask

COPY . .

EXPOSE 5000

CMD ["python", "app.py"]
```

---

## ⚙️ Docker Commands

### Create a Custom Network
```bash
docker network create ivolve-network
```

### Build Docker Images
```bash
docker build -t frontend-img ./frontend
docker build -t backend-img ./backend
```

### Run Containers

- Run backend on custom network:
```bash
docker run -d --name backend --network ivolve-network backend-img
```

- Run frontend1 on custom network:
```bash
docker run -d --name frontend1 --network ivolve-network frontend-img
```

- Run frontend2 on default network:
```bash
docker run -d --name frontend2 frontend-img
```

---

## 🔍 Verify Communication

- **From `frontend1` (should succeed):**
```bash
docker exec -it frontend1 curl http://backend:5000
```

![image](https://github.com/user-attachments/assets/7f4df9c2-c8dd-41d5-a454-dd454b3fdefe)

- **From `frontend2` (should fail):**
```bash
docker exec -it frontend2 curl http://backend:5000
```

![image](https://github.com/user-attachments/assets/6f9060ce-eeda-44e6-a40b-c08a6e82f7e3)

---

## 🧹 Cleanup

```bash
docker stop frontend1 frontend2 backend
docker rm frontend1 frontend2 backend
docker network rm ivolve-network
```
