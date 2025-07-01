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

# Optional: install ping for testing
RUN apt-get update && apt-get install -y iputils-ping

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

# Optional: install ping for testing
RUN apt-get update && apt-get install -y iputils-ping

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

- **From `frontend2` (should fail):**
```bash
docker exec -it frontend2 curl http://backend:5000
```

- **(Optional)** If using `ping`:
```bash
docker exec -it frontend1 ping backend
docker exec -it frontend2 ping backend
```

---

## 🧹 Cleanup

```bash
docker stop frontend1 frontend2 backend
docker rm frontend1 frontend2 backend
docker network rm ivolve-network
```