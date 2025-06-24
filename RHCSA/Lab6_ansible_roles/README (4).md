
# Lab 6: Deploy Jenkins with Docker using Ansible (Without community.docker)

## 🎯 Goal

The goal of this lab is to create an Ansible role that:
- Installs Docker on the managed node
- Pulls and runs the Jenkins container using the official Docker image
- Retrieves the initial Jenkins admin password
- Does **not use** the `community.docker` collection

This lab demonstrates how to automate Jenkins deployment using core Ansible modules like `shell` and `yum`.

---

## 🧱 Folder Structure

```
.
├── inventory
├── site.yml
└── roles/
    └── jenkins_docker_shell/
        └── tasks/
            └── main.yml
```

---

## 🛠️ Steps and Commands

### 1. 🔧 Create the role

```bash
ansible-galaxy init roles/jenkins_docker_shell
```

### 2. ✍️ Edit the role tasks

In `roles/jenkins_docker_shell/tasks/main.yml`:

```yaml
- name: Install Docker dependencies
  yum:
    name:
      - yum-utils
      - device-mapper-persistent-data
      - lvm2
    state: present

- name: Add Docker repo
  get_url:
    url: https://download.docker.com/linux/centos/docker-ce.repo
    dest: /etc/yum.repos.d/docker-ce.repo

- name: Install Docker
  yum:
    name: docker-ce
    state: present

- name: Enable and start Docker service
  service:
    name: docker
    state: started
    enabled: yes

- name: Pull Jenkins Docker image
  shell: docker pull jenkins/jenkins:lts
  args:
    creates: /var/lib/docker

- name: Run Jenkins container
  shell: |
    docker run -d       --name jenkins       -p 8080:8080 -p 50000:50000       -v jenkins_home:/var/jenkins_home       jenkins/jenkins:lts
  args:
    creates: /var/lib/docker/volumes/jenkins_home

- name: Wait for Jenkins to initialize and get admin password
  shell: docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
  register: jenkins_admin_password
  retries: 10
  delay: 10
  until: jenkins_admin_password.stdout != ""
  changed_when: false

- name: Show Jenkins admin password
  debug:
    msg: "Jenkins Initial Admin Password: {{ jenkins_admin_password.stdout }}"
```

---

### 3. 🗂️ Create inventory file

**`inventory`**
```ini
[app_servers]
servera ansible_host=192.168.X.X ansible_user=your_user ansible_ssh_private_key_file=~/.ssh/id_rsa
```

---

### 4. 📜 Create playbook

**`site.yml`**
```yaml
- name: Deploy Jenkins via Docker without community.docker
  hosts: app_servers
  become: true
  roles:
    - jenkins_docker_shell
```

---

### 5. ▶️ Run the Playbook

```bash
ansible-playbook -i inventory site.yml
```

---

## ✅ Final Result

- Jenkins is installed and running at `http://<your-managed-node-ip>:8080`
- The Jenkins initial admin password is displayed in the playbook output

---

## 🔐 Optional: Get Admin Password Manually

```bash
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

---

## 🧠 Notes

- This lab avoids external Ansible collections for environments without internet access.
- Docker must be allowed through the firewall (`port 8080`).
- Jenkins container is persisted using Docker volumes.

---

## 📌 Author

Created as part of Red Hat Automation & DevOps Lab – Task 6
