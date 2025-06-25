#  Lab1 Shell Script: MySQL Daily Backup Automation

## 📌 Task Goal

This project demonstrates how to automate daily backups of a MySQL database using a shell script and schedule it using a cron job.

The goal is to:
- Ensure regular backups of the MySQL database.
- Organize backups by date.
- Automate the entire process without manual intervention.

---

## 🛠️ Prerequisites

- Linux OS (Ubuntu/CentOS)
- MySQL server installed
- Bash shell
- User with permission to use `cron` and access MySQL

---

## 📂 Steps & Commands

### 1️⃣ Install MySQL Server

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mysql-server -y
```

**CentOS/RHEL:**
```bash
sudo dnf install mysql-server -y
sudo systemctl start mysqld
```

---

### 2️⃣ Create Backup Directory

```bash
mkdir -p ~/mysql_backups
```

---

### 3️⃣ Create Shell Script

Create a file named `mysql_backup.sh`:
```bash
nano mysql_backup.sh
```

Paste the following script:

```bash
#!/bin/bash

# Set variables
BACKUP_DIR=~/mysql_backups
DATE=$(date +%F)
FILENAME="MySQL_backup_$DATE.sql"

# Perform backup
mysqldump -u root -pYourPassword --all-databases > "$BACKUP_DIR/$FILENAME"
```

> 🔐 **Note:** Replace `YourPassword` with the actual MySQL root password or use a secure method like a `.my.cnf` file.

Make the script executable:
```bash
chmod +x mysql_backup.sh
```

---

### 4️⃣ Schedule with Cron

Edit the crontab:
```bash
crontab -e
```

Add this line to run the script daily at 5:00 PM:
```
0 17 * * * /full/path/to/mysql_backup.sh
```

You can find your current path using:
```bash
pwd
```

---

## 📦 Output

- Backup files are saved in the `~/mysql_backups` directory.
- Filenames will follow the format: `MySQL_backup_YYYY-MM-DD.sql`

Example:
```
MySQL_backup_2025-06-23.sql
```

---

## ✅ Benefits

- Ensures consistent, automatic database backups
- Organized storage with date-wise filenames
- Easy to set up and customize

---

