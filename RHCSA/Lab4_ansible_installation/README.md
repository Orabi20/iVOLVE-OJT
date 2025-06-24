
#  Lab 4: Ansible Installation and Configuration

## 🎯 Goal
The objective of this lab is to set up Ansible on a control node, configure a managed node for remote automation, and verify Ansible's functionality by executing ad-hoc commands.

---

## 📋 Tasks Overview

1. Install Ansible on the control node  
2. Create a custom inventory file for the managed node  
3. Generate an SSH key on the control node  
4. Copy the public key to the managed node  
5. Run an ad-hoc command to check connectivity and functionality  

---

## 🛠️ Step-by-Step Instructions

### ✅ 1. Install Ansible on the Control Node

```bash
sudo dnf install -y ansible
ansible --version
```

---

### ✅ 2. Create a Custom Inventory File

Create a file named `inventory`:

```bash
nano inventory
```

Add the following content:

```ini
[webservers]
servera
```


---

### ✅ 3. Generate an SSH Key Pair

On the control node:

```bash
ssh-keygen
```

- Press Enter to accept the default path
- Leave passphrase empty for simplicity

---

### ✅ 4. Copy the Public Key to the Managed Node

```bash
ssh-copy-id student@servera
```

> This sets up passwordless SSH access.

---

### ✅ 5. Verify SSH Connection (Optional Check)

```bash
ssh student@servera
```

If it logs in without a password, SSH setup is successful.

---

### ✅ 6. Run Ad-Hoc Command to Check Disk Space

```bash
ansible webservers -i inventory -m command -a "df -h"
```

You should see the disk usage output of the managed node.

---

## 🧪 Expected Result

The managed node should respond with `df -h` output, confirming Ansible is working properly and can execute remote commands.

---



## ✅ Conclusion

This lab demonstrates how to:
- Install Ansible
- Establish SSH communication between nodes
- Use a custom inventory
- Run ad-hoc commands to automate basic tasks

