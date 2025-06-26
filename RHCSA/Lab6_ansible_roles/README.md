# Lab 6: Ansible Roles for Application Deployment

## 🎯 Objective

Automate the installation of the following applications using Ansible roles:

- **Docker**
- **Kubernetes CLI (`kubectl`)**
- **Jenkins**

## 📁 Project Structure

```
lab6/
├── inventory.ini
├── main_role.yml
└── roles/
    ├── docker/
    │   └── tasks/
    │       └── main.yml
    ├── kubectl/
    │   └── tasks/
    │       └── main.yml
    └── jenkins/
        └── tasks/
            └── main.yml
```

## 🧪 Prerequisites

- Ansible installed on the control node
- SSH access to managed node(s)
- Managed nodes are Ubuntu-based
- Python installed on target hosts

## ⚙️ How to Use

### 1. Create the Roles

```bash
ansible-galaxy init roles/docker
ansible-galaxy init roles/kubectl
ansible-galaxy init roles/jenkins
```

### 2. Update Role Tasks

Copy the task content from `roles/*/tasks/main_role.yml` as provided.

### 3. Define Inventory

Create an `inventory` file with your target host(s):

```ini
[web_servers]
servera
```

### 4. Run the Playbook

```bash
ansible-playbook -i inventory.ini main_role.yml
```

## ✅ Verification

Check installed versions or services using Ansible ad-hoc commands:

```bash
ansible all -i inventory -m shell -a "docker --version"
ansible all -i inventory -m shell -a "kubectl version --client"
ansible all -i inventory -m shell -a "systemctl status jenkins"
```

## 📌 Notes

- Ensure your managed nodes can access external URLs (for Docker, Jenkins, and kubectl downloads).
