# Lab 7: Ansible Vault – MySQL Automation

## 🎯 Objective

Automate the installation and configuration of a MySQL server using Ansible. Secure sensitive variables such as the database user's password using **Ansible Vault**.

---

## ✅ Tasks Performed

1. Install MySQL server on the managed node.
2. Start and enable the MySQL service.
3. Create a new database named `iVolve`.
4. Create a new MySQL user with full privileges on the `iVolve` database.
5. Use **Ansible Vault** to encrypt the database user's password.
6. Validate the setup by listing databases as the created user.

---

## 🗂️ Folder Structure

```bash
lab7_ansible_vault/
├── inventory
├── mysql_setup.yml
├── group_vars/
│   └── all.yml           # Encrypted with Ansible Vault
├── vault_password_file.txt
└── README.md
```

---

## 🔐 Ansible Vault

Create an encrypted variable file:

```bash
ansible-vault create group_vars/all.yml --vault-password-file vault_password_file.txt
```

Sample content of `group_vars/all.yml`:

```yaml
db_user: ivolve_user
db_password: StrongPassword123!
```

---

## 📝 Ansible Playbook: `mysql_setup.yml`

```yaml
---
- name: Install and configure MySQL without using community collection
  hosts: db_servers
  become: yes
  vars_files:
    - group_vars/all.yml

  tasks:
    - name: Install MySQL server
      yum:
        name: mysql-server
        state: present

    - name: Start and enable MySQL
      service:
        name: mysqld
        state: started
        enabled: true

    - name: Wait for MySQL to be ready
      wait_for:
        path: /var/lib/mysql/mysql.sock
        state: present
        timeout: 30

    - name: Create iVolve database
      shell: |
        mysql -u root -e "CREATE DATABASE IF NOT EXISTS iVolve;"
      args:
        warn: false

    - name: Create MySQL user
      shell: |
        mysql -u root -e "CREATE USER IF NOT EXISTS '{{ db_user }}'@'%' IDENTIFIED BY '{{ db_password }}';"
      args:
        warn: false

    - name: Grant all privileges on iVolve to user
      shell: |
        mysql -u root -e "GRANT ALL PRIVILEGES ON iVolve.* TO '{{ db_user }}'@'%';"
      args:
        warn: false

    - name: Flush MySQL privileges
      shell: |
        mysql -u root -e "FLUSH PRIVILEGES;"
      args:
        warn: false

    - name: Validate: List databases as created user
      shell: |
        mysql -u {{ db_user }} -p{{ db_password }} -e "SHOW DATABASES;" | grep iVolve
      register: validation_output
      ignore_errors: true

    - name: Show validation output
      debug:
        var: validation_output.stdout_lines
```

---

## 🚀 Run the Playbook

```bash
ansible-playbook -i inventory mysql_setup.yml --vault-password-file vault_password_file.txt
```

---

## 📋 Notes

- No external collections used (e.g., `community.mysql`).
- Uses `shell` module for MySQL commands.
- Sensitive variables are secured with **Ansible Vault**.
- Compatible with RHEL/CentOS using `mysqld`.

---

## 🧠 Learning Points

- Secure secrets using **Ansible Vault**
- Automate MySQL installation and configuration
- Use `shell` to run raw MySQL commands
- Validate setup through command-line checks