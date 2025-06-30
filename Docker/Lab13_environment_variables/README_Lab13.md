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

### Method iii: Defined inside Dockerfile

No extra flags needed. Just run:
```bash
docker run -d -p 8080:8080 docker-lab13
```

---

## 🧪 Output Example

Access the app on:  
📍 `http://localhost:8080`

Expected output:
```
App mode: production, Region: canada-west
```

---

## 📝 Notes

- Make sure `app.py` reads environment variables and prints them in one line:
```python
from flask import Flask
import os

app = Flask(__name__)

@app.route('/')
def home():
    return f"App mode: {os.getenv('APP_MODE')}, Region: {os.getenv('APP_REGION')}"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
```

---
