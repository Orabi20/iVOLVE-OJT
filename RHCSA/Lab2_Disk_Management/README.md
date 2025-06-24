
# Lab2: Disk Partitioning and Logical Volume Management (XFS)

## 🎯 Objective

This lab simulates a real-world RHCSA-style task where a system administrator must:
- Add a new 6 GB disk to a CentOS VM.
- Split the disk into two partitions (2 GB and 3 GB).
- Create a Volume Group and Logical Volume using the 2 GB partition.
- Format the volume with the **XFS filesystem** and mount it.
- Extend the logical volume using the 3 GB partition and grow the filesystem **while mounted**.

---

## 🛠️ Environment

- OS: CentOS / RHEL 8 or later
- Privileges: Root or sudo access
- Filesystem: `XFS`
- Disk: Additional virtual disk (e.g., `/dev/sdb`, size 6 GB)

---

## 🧭 Steps & Commands

### 1️⃣ List available disks

```bash
lsblk
```

---

### 2️⃣ Partition the new disk (`/dev/sdb`)

```bash
sudo fdisk /dev/sdb
```

Inside `fdisk`:

```text
n     → Create new partition
p     → Primary
1     → Partition 1
+2G   → 2 GB size

n     → Create second partition
p     → Primary
2     → Partition 2
+3G   → 3 GB size

t → 1 → 8e   # Set type of partition 1 to Linux LVM
t → 2 → 8e   # Set type of partition 2 to Linux LVM

w     → Write changes
```

---

### 3️⃣ Reload partition table

```bash
sudo partprobe
```

---

### 4️⃣ Create Physical Volume on the 2 GB partition

```bash
sudo pvcreate /dev/sdb1
```

---

### 5️⃣ Create a Volume Group

```bash
sudo vgcreate rhcsa_task /dev/sdb1
```

---

### 6️⃣ Create a Logical Volume (LV)

```bash
sudo lvcreate -L 1.5G -n rhcsa_lv rhcsa_task
```

---

### 7️⃣ Format the LV with XFS

```bash
sudo mkfs.xfs /dev/rhcsa_task/rhcsa_lv
```

---

### 8️⃣ Mount the volume

```bash
sudo mkdir /mnt/rhcsa
sudo mount /dev/rhcsa_task/rhcsa_lv /mnt/rhcsa
```

---

### 9️⃣ Add the second partition to the volume group

```bash
sudo pvcreate /dev/sdb2
sudo vgextend rhcsa_task /dev/sdb2
```

---

### 🔟 Extend the Logical Volume

```bash
sudo lvextend -L +2G /dev/rhcsa_task/rhcsa_lv
```

---

### 🔁 Grow the XFS Filesystem (Must be mounted)

```bash
sudo xfs_growfs /mnt/rhcsa
```

---

### ✅ Verify

```bash
df -h /mnt/rhcsa
```

---

## 📌 Notes

- XFS does not support shrinking. Only **grow** operations are allowed.

---

