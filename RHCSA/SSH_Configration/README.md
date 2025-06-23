
# Lab 3: SSH Configurations

## 🎯 Goal

The goal of this lab is to configure passwordless SSH login from your local machine to a remote machine using SSH key authentication. After completing this lab, you’ll be able to connect to the remote machine simply by running:

```bash
ssh ivolve
```

---

## ✅ Objectives

1. Generate SSH key pair (public & private keys) on the local machine.
2. Copy the public key to the remote machine securely.
3. Configure an SSH alias to simplify future connections.

---

## 🛠️ Steps & Commands

### 1. Generate SSH Key Pair

Run this command on your **local machine**:

```bash
ssh-keygen
```

- Press `Enter` to accept the default save location (`~/.ssh/id_rsa`)
- Enter a passphrase (optional)

This will generate:
- `~/.ssh/id_rsa` (private key)
- `~/.ssh/id_rsa.pub` (public key)

---

### 2. Copy the Public Key to the Remote Machine

Assuming the remote machine’s username is `user` and its hostname or IP is `ivolve` or `192.168.1.10`:

```bash
ssh-copy-id user@ivolve
```

Or:

```bash
ssh-copy-id user@192.168.1.10
```

This copies your public key to the `~/.ssh/authorized_keys` file on the remote machine.

---

### 3. Configure SSH Alias for Simplicity

On your **local machine**, create or edit the SSH config file:

```bash
nano ~/.ssh/config
```

Add the following:

```ssh
Host ivolve
    HostName 192.168.1.10   # Replace with the actual IP or hostname
    User user               # Replace with your username
    IdentityFile ~/.ssh/id_rsa
```

Save and exit (`CTRL+O`, `Enter`, `CTRL+X` in nano).

---

### 4. Connect Using the Alias

Now you can connect easily:

```bash
ssh ivolve
```

No need to enter a password if everything is set up correctly.

---

## 🔐 Notes

- Never share your private key (`id_rsa`) with anyone.
- You can use different SSH keys per host by specifying unique `IdentityFile` entries in the SSH config.

---
