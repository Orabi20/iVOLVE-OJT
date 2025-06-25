
# Lab 8: Ansible Dynamic Inventory with AWS EC2

## 🎯 Objective

Configure Ansible to use **dynamic inventory** with the `aws_ec2` plugin and apply a playbook that sets up **Nginx** on EC2 instances tagged with `Name=ivolve`.

---

## ✅ Prerequisites

- An AWS EC2 instance launched with:
  - Tag: `Name=ivolve`
  - A reachable public IP
  - Security group allowing SSH (port 22) and HTTP (port 80)
- A valid SSH key (`ivolvekey.pem`) with permission set to `chmod 400`
- Ansible 2.13 or later
- `amazon.aws` collection installed

---

## 🗂️ Dynamic Inventory File (`inventory/aws_ec2.yml`)

```yaml
plugin: amazon.aws.aws_ec2
regions:
  - us-east-1
filters:
  tag:Name: ivolve
keyed_groups:
  - key: tags.Name
    prefix: tag
hostnames:
  - public-ip-address
compose:
  ansible_host: public_ip_address
  ansible_user: ec2-user
```

---

## 🛠️ Playbook: `nginx_lab5.yml`

```yaml
---
- name: Configure Nginx Web Server on EC2s tagged "ivolve"
  hosts: tag_ivolve
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

## ▶️ How to Run

```bash
ansible-playbook nginx_lab5.yml   -i inventory/aws_ec2.yml   --private-key ivolvekey.pem
```

---

## 👤 Author

**Ahmed Orabi**
