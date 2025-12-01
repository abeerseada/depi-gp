# DevOps Graduation Project

<p align="center">
      
  <img src="static/depi.png" height="90"/>
</p>

<h3 align="center">In Collaboration with Depi </h3>

<p align="center">
  Final project for the DEPI DevOps program, containerizing and orchestrating a Python web app using Docker and Kubernetes.
</p>

<h3 align="center"> This is the architecture diagram </h3>
<p align="center">
<img src="static/draw.gif" width="800">
</p>

---

# **DEPI Graduation Project: Full DevOps CI/CD Pipeline**

A complete DevOps pipeline built using **Terraform**, **Ansible**, **Jenkins**, **Docker**, **Kubernetes**, and **ArgoCD**, enabling automated build and deployment from GitHub commits to a running Kubernetes application.

---

## **Project Overview**

This project implements a fully automated CI/CD workflow:

- **Terraform** provisions AWS infrastructure (VPC, Subnets, EC2 instances).
- **Ansible** configures Jenkins Master and Slave nodes.
- **Jenkins** builds, scans, and pushes Docker images.
- **ArgoCD** deploys updated Kubernetes manifests automatically.
- **DockerHub** hosts application images.
- **GitHub Webhooks** trigger Jenkins on every code push.

This creates a complete end-to-end GitOps workflow.

---

## **Architecture Summary**

### **Infrastructure (Terraform)**

- VPC CIDR: `10.0.0.0/16`
- Public Subnet: `10.0.1.0/24`
- Private Subnet: `10.0.10.0/24`
- Internet Gateway + NAT Gateway
- EC2 Instances:

  - **Jenkins Master** (Public subnet)
  - **Jenkins Slave/Agent** (Private subnet)

- S3 bucket to store Terraform state

### **Configuration (Ansible)**

- Install Docker, Git, Java
- Configure Jenkins Master container
- Prepare Jenkins Slave as a build agent

### **CI (Jenkins)**

Pipeline stages:

1. Checkout source from GitHub
2. Build Docker image
3. Scan image with Trivy
4. Push image to DockerHub
5. Update Kubernetes manifests
6. Commit and push changes back to GitHub

### **CD (ArgoCD + Kubernetes)**

- ArgoCD watches the `k8s/` folder in the repo
- Auto-deploys whenever image tags change
- Kubernetes hosts the running application in namespace `depi`

---

## **Terraform Deployment**

### **Initialize & Apply**

```bash
cd terraform
terraform init
terraform plan -out plan.tfplan
terraform apply "plan.tfplan"
```

Terraform automatically stores state in the S3 bucket configured in `backend.tf`.

---

## **Ansible Configuration**

### **Run playbooks**

```bash
cd ansible
ansible-playbook -i hosts.ini jenkins_master.yml
ansible-playbook -i hosts.ini prepare_slave.yml
```

This sets up:

- Docker engine
- Jenkins Master container
- Jenkins Slave tooling
- Required permissions (docker group, SSH, etc.)

---

## **Jenkins Pipeline**

Each stage is written as a Groovy shared library step under `ci/steps/`.

### **Main Pipeline (`Jenkinsfile`) includes:**

- Build Docker image
- Delete local image (optional)
- Push image to DockerHub
- Security scan with Trivy
- Update Kubernetes YAML files
- Commit & push back to GitHub

Webhook from GitHub triggers the pipeline automatically.

---

## **Kubernetes + ArgoCD Deployment**

### **Application Definition**

Located at:

```
k8s/
 ├─ namespace.yaml
 ├─ deployment.yaml
 └─ service.yaml
```

### **ArgoCD Application**

<p align="center">
<img src="static/argocd.png" width="800">
<img src="static/app.png" width="800">
</p>

### **ArgoCD Sync**

```bash
argocd app sync myapp
argocd app get myapp
```

---

## **End-to-End Workflow**

1. Developer pushes code → GitHub
2. GitHub webhook triggers Jenkins
3. Jenkins builds + scans + pushes Docker image
4. Jenkins updates Kubernetes manifests with the new image tag
5. ArgoCD detects the commit and auto-syncs
6. Kubernetes updates the running application automatically

---

## **Useful Commands**

### **ArgoCD**

```bash
kubectl -n argocd port-forward svc/argocd-server 8080:443
argocd login localhost:8080
argocd app list
```

### **Kubernetes**

```bash
kubectl get pods -n depi
kubectl describe deployment -n depi
```

### **Terraform**

```bash
terraform destroy
```

### **Jenkins**

```bash
docker run -d --name jenkins \
  --restart=always \
  -p 8080:8080 -p 50000:50000 \
  -v jenkins_data:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v /usr/bin/docker:/usr/bin/docker \
  --group-add 999 \
  jenkins/jenkins:lts-jdk11
```

---

## **Conclusion**

This project demonstrates a real-world DevOps workflow:

- Infrastructure as Code
- Automated configuration
- Continuous Integration
- Continuous Delivery
- GitOps deployment through ArgoCD

Fully automated from code commit → to running application.
