# Lab 13: Docker Environment Variables

This lab demonstrates how to work with environment variables in Docker using a Flask Python application.

---

## 🔧 Project Setup

### 1. Clone the Repository
```bash
git clone https://github.com/Ibrahim-Adel15/Docker-3.git
cd Docker-3
```

---

## 🐳 Dockerfile

### Dockerfile Content:
```dockerfile
FROM python:3.9-slim

# Set default environment variables (production, canada-west)
ENV APP_MODE=production
ENV APP_REGION=canada-west

WORKDIR /app
COPY . /app

RUN pip install flask

EXPOSE 8080

CMD ["python", "app.py"]
```

---

## 🧪 Environment Variable Injection

### Method i: Run with environment variables via CLI
```bash
docker run -d -p 8080:8080 -e APP_MODE=development -e APP_REGION=us-east docker-lab13
```
## 🧪 Output

Access the app on:  
📍 `http://localhost:8080`
![image](https://github.com/user-attachments/assets/11eddb32-8ea0-441b-bab4-7092e7065bcb)

### Method ii: Use environment variables from file

Create a file named `.env.staging`:
```
APP_MODE=staging
APP_REGION=us-west
```

Then run:
```bash
docker run -d -p 8080:8080 --env-file .env.staging docker-lab13
```
## 🧪 Output

Access the app on:  
📍 `http://localhost:8080`
![image](https://github.com/user-attachments/assets/fa277af2-4bbf-4c72-b628-fa58609f0304)

### Method iii: Defined inside Dockerfile

No extra flags needed. Just run:
```bash
docker run -d -p 8080:8080 docker-lab13
```

---

## 🧪 Output

Access the app on:  
📍 `http://localhost:8080`

![image](https://github.com/user-attachments/assets/d88eb98d-4ab9-451c-83f7-40d06e615ae5)

---



if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
```

---
