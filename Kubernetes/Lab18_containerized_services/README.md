# Lab 18: Containerized Node.js and MySQL Stack Using Docker Compose

## 📌 Objective

Containerize a Node.js application with a MySQL backend using Docker Compose.

---

## 🔗 Clone the Application

```bash
git clone https://github.com/Ibrahim-Adel15/kubernetes-app.git
cd kubernetes-app
```

---

## ⚙️ Docker Compose Configuration

### 📁 `.env` (optional but recommended)

```env
DB_HOST=db
DB_USER=root
DB_PASSWORD=rootpassword
MYSQL_ROOT_PASSWORD=rootpassword
```

### 🐳 `docker-compose.yml`

```yaml
version: "3.8"

services:
  app:
    build: .
    ports:
      - "3000:3000"
    environment:
      - DB_HOST=${DB_HOST}
      - DB_USER=${DB_USER}
      - DB_PASSWORD=${DB_PASSWORD}
    depends_on:
      - db

  db:
    image: mysql:5.7
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - MYSQL_DATABASE=ivolve
    volumes:
      - db_data:/var/lib/mysql

volumes:
  db_data:
```

---

## 🧪 Verify Application

### 1. Run the containers:

```bash
docker-compose up --build
```

### 2. Check endpoints:

```bash
curl http://localhost:3000
curl http://localhost:3000/health
curl http://localhost:3000/ready
```

### 3. View app logs:

```bash
docker-compose exec app cat /app/logs/<logfile>
```

---

## 📦 Push Docker Image to Docker Hub

```bash
# Tag and push your image
docker build -t yourdockerhubusername/kubernetes-app .
docker login
docker push yourdockerhubusername/kubernetes-app
```

---

## ✅ Expected Log Output

```
✅ Connected to MySQL and 'ivolve' DB found.
🚀 Server started on http://localhost:3000
```

---


## 🏁 Done!

You now have a fully containerized Node.js and MySQL stack using Docker Compose 🚀
