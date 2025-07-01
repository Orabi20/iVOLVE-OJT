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
cd /nginx-bind/html/
vim index.html  #set your html page code
```

### 3. Run Nginx Container
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
```
![image](https://github.com/user-attachments/assets/b394d4e0-d536-428d-9a65-b88bf821aa98)

### 6. Modify `index.html` and Retest
```bash
echo "Updated Page" > ~/nginx-bind/html/index.html
curl http://localhost:8080
```
![image](https://github.com/user-attachments/assets/fb3d70bc-2bc4-476d-85eb-9065ef675795)

### 7. Access Logs from Volume (via container)
```bash
docker exec -it nginx-lab14 sh
cd /var/log/nginx
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
