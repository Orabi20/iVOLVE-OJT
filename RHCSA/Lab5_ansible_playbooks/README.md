
#  Lab 5: Ansible Playbook for Web Server Configuration

## 🎯 Goal

The goal of this lab is to write and execute an Ansible playbook that automates the installation and configuration of an Nginx web server on a managed node. This includes:
- Installing Nginx
- Starting and enabling the service
- Customizing the default web page
- Opening the HTTP port (80) in the firewall

This lab demonstrates basic automation using Ansible playbooks, privilege escalation (`become`), and configuration verification.

---

## 🧰 Requirements

- Control Node with Ansible installed
- Managed Node (e.g., CentOS or RHEL VM)
- SSH access between control and managed node
- Sudo privileges on the managed node

---

## 📁 Repository Structure

```
├── inventory.ini
├── webserver.yml
└── README.md
```

---

## 🛠️ Steps

### 1. 🔑 Configure SSH Access
Generate SSH key on control node (if not done):
```bash
ssh-keygen
```

Copy key to managed node:
```bash
ssh-copy-id student@servera
```

---

### 2. 📦 Create Inventory File

**File: `inventory.ini`**
```ini
[web_servers]
servera
```

---

### 3. 📜 Create the Playbook

**File: `webserver.yml`**

```yaml
---
- name: Configure Nginx Web Server
  hosts: web_servers
  become: true
  tasks:

    - name: Install Nginx
      ansible.builtin.yum:
        name: nginx
        state: present

    - name: Ensure Nginx is started and enabled
      ansible.builtin.service:
        name: nginx
        state: started
        enabled: yes

    - name: Customize the Nginx homepage
      ansible.builtin.copy:
        dest: /usr/share/nginx/html/index.html
        content: |
          <!DOCTYPE html>
          <html>
          <head><title>Lab5</title></head>
          <body><h1>Lab5 by Ahmed Orabi</h1></body>
          </html>
        owner: root
        group: root
        mode: '0644'

    - name: Open HTTP port in firewall
      ansible.builtin.firewalld:
        port: 80/tcp
        permanent: yes
        state: enabled
        immediate: yes
      when: ansible_facts['os_family'] == "RedHat"
```

---

### 4. 🚀 Run the Playbook

```bash
ansible-playbook -i inventory.ini webserver.yml --ask-become-pass
```

---

### 5. ✅ Verify the Setup

On your control node or browser, test the server:
```bash
curl http://192.168.56.10
```

You should see your custom HTML page.

![image](https://github.com/user-attachments/assets/01e29a48-f806-4994-9818-c6733f51f9d7)

---

