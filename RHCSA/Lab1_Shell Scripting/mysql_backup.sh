#!/bin/bash
BACKUP_DIR=/root/mysql_backups
DATE=$(date +%F)
FILENAME="MySQL_backup_$DATE.sql"
mysqldump -u root -p 2023 --all-databases > "$BACKUP_DIR/$FILENAME"

