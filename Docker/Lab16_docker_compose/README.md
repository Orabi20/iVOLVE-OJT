
# Lab 16: Docker Compose - Node.js + PostgreSQL App

## 📌 Objective

Set up a multi-container application using Docker Compose that includes:

- A Node.js application
- A PostgreSQL database
- A shared Docker network and persistent volume

---

## 🧱 Project Structure

```
docker6/
├── app.js
├── package.json
├── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## 🧰 Technologies Used

- Node.js
- PostgreSQL 15 (Alpine)
- Docker
- Docker Compose

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Ibrahim-Adel15/docker6.git
cd docker6
```

### 2. Create the `docker-compose.yml`

Make sure this file exists in the root directory:

```yaml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "3000:3000"
    depends_on:
      - db
    networks:
      - mynet

  db:
    image: postgres:15-alpine
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: postgres
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - mynet

volumes:
  postgres_data:

networks:
  mynet:
```

---

## 🐳 Running the App

Start everything using:

```bash
docker compose up -d
```

OR (if using older Docker):

```bash
docker-compose up -d
```

---

## 🔍 Access

- **Web App:** [http://localhost:3000](http://localhost:3000)
- **PostgreSQL:** Connect to `db:5432` from inside the app using:
  - **Host:** `db`
  - **User:** `postgres`
  - **Password:** `postgres`
  - **Database:** `postgres`

---

## 📂 Data Persistence

PostgreSQL data is stored in a named volume:
```bash
postgres_data -> /var/lib/postgresql/data
```

---

## 🧹 Cleanup

Stop and remove containers, network, and volume:

```bash
docker compose down -v
```

<img width="664" alt="lab16 1" src="https://github.com/user-attachments/assets/57f050c9-e2a8-4c3b-81a3-a841cbf3d21f" />

---

## 📝 Notes

- Make sure Docker and Docker Compose are installed.
- Use `docker compose logs -f` to monitor logs.
- Use `docker ps` to verify containers are running.

---

