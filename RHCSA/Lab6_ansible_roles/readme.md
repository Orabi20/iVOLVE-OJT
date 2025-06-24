# Lab 6: Ansible Roles for Application Deployment

## 🎯 Objective

Automate the installation of the following tools on a managed node using **Ansible roles**:

- Docker
- Kubernetes CLI (`kubectl`)
- Jenkins

Each tool is managed using a **separate Ansible Galaxy role**.

---

## 📁 Project Structure

Ansible_Task/
├── inventory # Inventory file for managed nodes
├── site.yml # Master playbook that applies all roles
└── roles/
├── docker_role/ # Role to install and configure Docker
├── kubectl_role/ # Role to install kubectl
└── jenkins_role/ # Role to install and configure Jenkins

yaml
Copy
Edit

---

## ⚙️ Roles Breakdown

### 1. `docker_role`

- Installs Docker using `yum`
- Enables and starts the Docker service

### 2. `kubectl_role`

- Downloads the latest stable version of `kubectl`
- Installs it to `/usr/local/bin/` with execution permissions

### 3. `jenkins_role`

- Adds Jenkins YUM repo and imports GPG key
- Installs Jenkins using `yum`
- Starts and enables Jenkins service

---

## 📝 Inventory Example (`inventory`)

```ini
[managed_nodes]
servera.lab.example.com ansible_user=student ansible_ssh_private_key_file=~/.ssh/id_rsa
🚀 How to Run
Navigate to your project directory:

bash
Copy
Edit
cd ~/Ansible_Task
Run the playbook:

bash
Copy
Edit
ansible-playbook -i inventory site.yml
✅ Verification Commands
After execution, verify installations using:

bash
Copy
Edit
ansible managed_nodes -m shell -a "docker --version"
ansible managed_nodes -m shell -a "kubectl version --client"
ansible managed_nodes -m shell -a "systemctl status jenkins"
🧰 Useful Ansible Commands
Create a new role:

bash
Copy
Edit
ansible-galaxy init <role_name>
Run a specific playbook:

bash
Copy
Edit
ansible-playbook -i inventory site.yml
📌 Notes
Ensure ansible_user has sudo privileges.

Ensure SSH key-based authentication is set up between control and managed node.

kubectl installation uses a dynamic URL; for air-gapped environments, use static binaries.
