# Lab 14: Docker Volumes and Bind Mount with Nginx

## 🎯 Objective
Set up an Nginx container using:
- A **Docker volume** to persist Nginx logs.
- A **bind mount** to serve a custom HTML page.

---

## 🧪 Steps

### 1. Create Docker Volume for Logs
```bash
docker volume create nginx_logs
```

### 2. Create Local Directory and `index.html`
```bash
mkdir -p ~/nginx-bind/html
echo "Hello from Bind Mount" > ~/nginx-bind/html/index.html
```

### 3. Set Correct Permissions
```bash
chmod -R 755 ~/nginx-bind
chmod 644 ~/nginx-bind/html/index.html
```

### 4. Run Nginx Container
> Replace `/home/yourusername` with your actual path (no `~`)
```bash
docker run -d \
  --name nginx-lab14 \
  -v nginx_logs:/var/log/nginx \
  -v /home/yourusername/nginx-bind/html:/usr/share/nginx/html \
  -p 8080:80 \
  nginx
```

### 5. Test Access from Browser or curl
```bash
curl http://localhost:8080
# Output: Hello from Bind Mount
```

### 6. Modify `index.html` and Retest
```bash
echo "Updated Page" > ~/nginx-bind/html/index.html
curl http://localhost:8080
# Output: Updated Page
```

### 7. Access Logs from Volume (via container)
```bash
docker exec -it nginx-lab14 sh
cd /var/log/nginx
cat access.log
```

### 8. Inspect Volume via Temporary Container
```bash
docker run --rm -it -v nginx_logs:/mnt alpine sh
cd /mnt
ls -l
cat access.log
```

### 9. Find Docker Volume on Host (Linux)
```bash
sudo ls /var/lib/docker/volumes/nginx_logs/_data
```

### 10. Clean Up
```bash
docker rm -f nginx-lab14
docker volume rm nginx_logs
```

---

## 📂 Reference
To get the full path of a volume:
```bash
docker volume inspect nginx_logs
```
This shows the host mount path, e.g.:
`/var/lib/docker/volumes/nginx_logs/_data`

---

✅ You now have a working setup using both **Docker volume** and **bind mount** with Nginx!