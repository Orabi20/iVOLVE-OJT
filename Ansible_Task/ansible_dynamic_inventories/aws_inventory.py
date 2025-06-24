# save as generate_inventory.py

import boto3
import json

ec2 = boto3.client('ec2')

response = ec2.describe_instances(
    Filters=[{'Name': 'tag:Name', 'Values': ['ivolve']}, {'Name': 'instance-state-name', 'Values': ['running']}]
)

hosts = []

for reservation in response['Reservations']:
    for instance in reservation['Instances']:
        public_ip = instance.get('PublicIpAddress')
        if public_ip:
            hosts.append(public_ip)

# Output static inventory format
inventory = {
    "all": {
        "hosts": hosts,
        "vars": {
            "ansible_user": "ec2-user",
            "ansible_ssh_private_key_file": "~/.ssh/your-key.pem"
        }
    }
}

with open('inventory.json', 'w') as f:
    json.dump(inventory, f, indent=2)

print("Inventory written to inventory.json")
fix: tag
