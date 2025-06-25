
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

**File: `web_servers.yml`**

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
          <html lang="en">
          <head>
              <meta charset="UTF-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0" />
              <title>Lab5 by Ahmed Orabi</title>
              <style>
                  @import url('https://fonts.googleapis.com/css2?family=Red+Hat+Display:wght@500;700&display=swap');
                  body {
                      margin: 0;
                      font-family: 'Red Hat Display', sans-serif;
                      background: linear-gradient(135deg, #e6e6e6, #ffffff);
                      color: #1a1a1a;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      height: 100vh;
                  }
                  .container {
                      text-align: center;
                      padding: 40px;
                      border: 4px solid #cc0000;
                      border-radius: 20px;
                      background: #fff5f5;
                      box-shadow: 0 0 20px rgba(204, 0, 0, 0.3);
                  }
                  h1 {
                      font-size: 4rem;
                      color: #cc0000;
                      margin-bottom: 10px;
                  }
                  p {
                      font-size: 1.8rem;
                      color: #333;
                  }
              </style>
          </head>
          <body>
              <div class="container">
                  <h1>Lab5</h1>
                  <p>by Ahmed Orabi</p>
              </div>
          </body>
          </html>
        owner: root
        group: root
        mode: '0644'
        
    - name: Open HTTP port using firewalld-cmd
      command: firewall-cmd --permanent --add-service=http
      notify: reload firewall
      
  handlers:
    - name: reload firewall
      ansible.builtin.service:
        name: firewalld
        state: reloaded

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

