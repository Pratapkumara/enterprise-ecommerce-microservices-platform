# 🚀 Enterprise E-Commerce Microservices Platform

<p align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024-blue?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker)
![Kubernetes](https://img.shields.io/badge/Kubernetes-Deployed-326CE5?style=for-the-badge&logo=kubernetes)
![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-D24939?style=for-the-badge&logo=jenkins)
![Prometheus](https://img.shields.io/badge/Monitoring-Prometheus-E6522C?style=for-the-badge&logo=prometheus)
![Grafana](https://img.shields.io/badge/Grafana-Dashboard-F46800?style=for-the-badge&logo=grafana)
![SonarQube](https://img.shields.io/badge/SonarQube-Code%20Quality-4E9BCD?style=for-the-badge&logo=sonarqube)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-336791?style=for-the-badge&logo=postgresql)

</p>

---

# 📖 Project Overview

Enterprise E-Commerce Microservices Platform is a production-style backend application developed using **Spring Boot Microservices** and modern **DevOps** practices.

The project demonstrates how large enterprise applications are designed using independent microservices, centralized configuration, service discovery, API Gateway, containerization, monitoring, CI/CD, and Kubernetes.

Each business capability is developed as an independent Spring Boot service, making the application highly scalable, maintainable, fault-tolerant, and cloud-ready.

This project follows real enterprise architecture used in modern software companies.

---

# 🎯 Objectives

- Build scalable Spring Boot Microservices
- Implement JWT Authentication & Authorization
- Centralize configuration using Spring Cloud Config
- Service Discovery using Eureka
- API Routing using Spring Cloud Gateway
- Containerize every service using Docker
- Build Jenkins CI/CD Pipeline
- Monitor application using Prometheus & Grafana
- Configure Alertmanager
- Deploy complete platform on Kubernetes
- Follow Enterprise DevOps practices

---

# ✨ Features

## Authentication

- JWT Authentication
- User Registration
- User Login
- Role Based Authentication
- BCrypt Password Encryption
- Stateless Security

---

## Product Module

- Add Product
- Update Product
- Delete Product
- View Products
- Category Support
- Quantity Management

---

## Inventory Module

- Inventory Tracking
- Stock Availability
- Inventory Updates
- Database Synchronization

---

## Order Module

- Create Order
- Order Management
- Order Processing
- Product Validation

---

## Payment Module

- Payment Processing
- Payment Status
- Database Storage

---

## Notification Module

- Notification Service
- Service Communication
- Event Processing

---

## Cloud Features

- Spring Cloud Config Server
- Eureka Service Discovery
- API Gateway
- Centralized Configuration
- Dynamic Service Registration

---

## DevOps Features

- Docker Containerization
- Docker Compose
- Jenkins CI/CD
- SonarQube Code Analysis
- Prometheus Monitoring
- Grafana Dashboards
- Alertmanager Alerts
- Kubernetes Deployment

---

# 🏗️ System Architecture

```
                    Client
                       │
                       ▼
               API Gateway
                       │
      ┌────────────────┼────────────────┐
      ▼                ▼                ▼
 User Service    Product Service   Inventory Service
      │                │                │
      └────────────┐   │                │
                   ▼   ▼                ▼
             Order Service       Payment Service
                   │
                   ▼
          Notification Service

                   │
                   ▼
              PostgreSQL Database

────────────────────────────────────────────

Config Server
       ▲
       │
All Microservices

────────────────────────────────────────────

Eureka Discovery Server
       ▲
       │
All Services Register

────────────────────────────────────────────

Jenkins
      │
Docker Build
      │
Docker Images
      │
Kubernetes Deployment

────────────────────────────────────────────

Prometheus
      │
Grafana
      │
Alertmanager
```

---

# 🛠️ Technology Stack

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Cloud
- Spring Data JPA
- Spring Validation
- Maven

---

## Cloud

- Spring Cloud Config
- Eureka Discovery Server
- Spring Cloud Gateway

---

## Security

- JWT
- BCrypt Password Encoder
- Authentication Manager
- Authorization
- Role Based Security

---

## Database

- PostgreSQL

---

## Build Tool

- Maven

---

## Containerization

- Docker
- Docker Compose

---

## CI/CD

- Jenkins

---

## Code Quality

- SonarQube

---

## Monitoring

- Prometheus
- Grafana
- Alertmanager
- Node Exporter
- cAdvisor

---

## Container Orchestration

- Kubernetes
- Minikube

---

## Version Control

- Git
- GitHub

---

# 📦 Microservices

| Service | Port | Description |
|----------|------|-------------|
| Config Server | 8888 | Centralized Configuration |
| Eureka Server | 8761 | Service Discovery |
| API Gateway | 8082 | API Routing |
| User Service | 8083 | Authentication & Users |
| Product Service | 8084 | Product Management |
| Inventory Service | 8086 | Inventory |
| Order Service | 8087 | Orders |
| Payment Service | 8088 | Payments |
| Notification Service | 8089 | Notifications |

---

# 🗂️ Project Structure

```text
Enterprise-Ecommerce-Microservices-Platform
│
├── api-gateway
├── config-server
├── discovery-server
├── user-service
├── product-service
├── inventory-service
├── order-service
├── payment-service
├── notification-service
│
├── docker-compose.yml
├── Jenkinsfile
├── k8s
│
├── config-repository
│
├── monitoring
│   ├── prometheus
│   ├── grafana
│   └── alertmanager
│
└── README.md
```

---

# 🚀 Project Status

| Module | Status |
|---------|--------|
| Microservices | ✅ Completed |
| JWT Security | ✅ Completed |
| Config Server | ✅ Completed |
| Eureka Discovery | ✅ Completed |
| API Gateway | ✅ Completed |
| PostgreSQL | ✅ Completed |
| Docker | ✅ Completed |
| Jenkins | ✅ Completed |
| SonarQube | ✅ Completed |
| Prometheus | ✅ Completed |
| Grafana | ✅ Completed |
| Alertmanager | ✅ Completed |
| Kubernetes | ✅ Completed |
| End-to-End Testing | ✅ Completed |

---

## 📌 Next

➡️ **Part 2** will include:

- Installation Guide
- Local Setup
- Docker Deployment
- Docker Compose
- Kubernetes Deployment
- Jenkins Pipeline
- CI/CD Workflow
- Configuration Repository
- Environment Variables

```
---

# ⚙️ Prerequisites

Before running this project, make sure the following software is installed:

| Software | Version |
|----------|---------|
| Java | 21 |
| Maven | 3.9+ |
| Docker | Latest |
| Docker Compose | Latest |
| Kubernetes | v1.30+ |
| Minikube | Latest |
| kubectl | Latest |
| Git | Latest |
| PostgreSQL | 16+ |
| Jenkins | Latest |

---

# 🚀 Getting Started

Clone the repository:

```bash
git clone https://github.com/<YOUR_GITHUB_USERNAME>/enterprise-ecommerce-microservices-platform.git

cd enterprise-ecommerce-microservices-platform
```

---

# 🗄 Database Configuration

Create the following PostgreSQL databases:

```
userdb
productdb
inventorydb
orderdb
paymentdb
notificationdb
```

Default credentials:

```
Username : postgres
Password : postgres
```

---

# ⚙ Spring Cloud Config Server

The project uses a centralized configuration repository.

```
config-repository/

├── api-gateway.yml
├── user-service.yml
├── product-service.yml
├── inventory-service.yml
├── order-service.yml
├── payment-service.yml
├── notification-service.yml
```

All services load their configuration from the Config Server during startup.

---

# ▶ Running Microservices Locally

Start the services in the following order:

1. Config Server

```
cd config-server
mvn spring-boot:run
```

---

2. Eureka Discovery Server

```
cd discovery-server
mvn spring-boot:run
```

---

3. User Service

```
cd user-service
mvn spring-boot:run
```

---

4. Product Service

```
cd product-service
mvn spring-boot:run
```

---

5. Inventory Service

```
cd inventory-service
mvn spring-boot:run
```

---

6. Order Service

```
cd order-service
mvn spring-boot:run
```

---

7. Payment Service

```
cd payment-service
mvn spring-boot:run
```

---

8. Notification Service

```
cd notification-service
mvn spring-boot:run
```

---

9. API Gateway

```
cd api-gateway
mvn spring-boot:run
```

---

# 🐳 Docker Deployment

Build Docker images for all services.

Example:

```bash
cd user-service

docker build -t user-service:1.0 .
```

Repeat for every microservice.

Verify Docker images:

```bash
docker images
```

---

# 🐳 Docker Compose

Start the complete application:

```bash
docker compose up -d
```

Stop the application:

```bash
docker compose down
```

View running containers:

```bash
docker ps
```

View logs:

```bash
docker compose logs -f
```

---

# ☸ Kubernetes Deployment

Start Minikube:

```bash
minikube start
```

Enable Docker environment:

```bash
eval $(minikube docker-env)
```

Deploy Config Server:

```bash
kubectl apply -f k8s/config-server/
```

Deploy Eureka:

```bash
kubectl apply -f k8s/eureka-server/
```

Deploy User Service:

```bash
kubectl apply -f k8s/user-service/
```

Deploy Product Service:

```bash
kubectl apply -f k8s/product-service/
```

Deploy Inventory Service:

```bash
kubectl apply -f k8s/inventory-service/
```

Deploy Order Service:

```bash
kubectl apply -f k8s/order-service/
```

Deploy Payment Service:

```bash
kubectl apply -f k8s/payment-service/
```

Deploy Notification Service:

```bash
kubectl apply -f k8s/notification-service/
```

Deploy API Gateway:

```bash
kubectl apply -f k8s/api-gateway/
```

---

Verify Deployments:

```bash
kubectl get deployments
```

Verify Pods:

```bash
kubectl get pods
```

Verify Services:

```bash
kubectl get svc
```

---

Get API Gateway URL:

```bash
minikube service api-gateway --url
```

Example Output:

```
http://192.168.xx.xx:32xxx
```

---

# 🔄 Jenkins CI/CD Pipeline

The Jenkins pipeline performs the following steps:

- Clone GitHub Repository
- Build using Maven
- Execute Unit Tests
- Run SonarQube Analysis
- Build Docker Images
- Push Build Artifacts
- Deploy Application

Pipeline Stages:

```
Git Checkout
      │
      ▼
 Maven Build
      │
      ▼
 Unit Testing
      │
      ▼
 SonarQube Analysis
      │
      ▼
 Docker Build
      │
      ▼
 Deployment
```

---

# 📊 Monitoring Stack

Monitoring components:

- Prometheus
- Grafana
- Alertmanager
- Node Exporter
- cAdvisor

Metrics collected:

- CPU Usage
- Memory Usage
- Disk Usage
- Network Traffic
- Container Metrics
- JVM Metrics
- Application Health
- Service Availability

---

# 🔐 Security

Implemented security features:

- JWT Authentication
- BCrypt Password Encoding
- Spring Security
- Stateless Authentication
- Authorization Filter
- Protected REST APIs
- Public Authentication Endpoints

---
# 🌐 REST API Documentation

## Authentication APIs

### Register User

```
POST /api/v1/users/register
```

Request Body

```json
{
  "firstName":"John",
  "lastName":"Doe",
  "email":"john@example.com",
  "password":"Password123",
  "role":"USER"
}
```

---

### Login

```
POST /api/v1/auth/login
```

Request

```json
{
  "email":"john@example.com",
  "password":"Password123"
}
```

Response

```json
{
  "token":"JWT_TOKEN",
  "tokenType":"Bearer"
}
```

---

## Product APIs

| Method | Endpoint |
|----------|------------------------------|
| GET | /api/v1/products |
| GET | /api/v1/products/{id} |
| POST | /api/v1/products |
| PUT | /api/v1/products/{id} |
| DELETE | /api/v1/products/{id} |

---

## Inventory APIs

| Method | Endpoint |
|----------|------------------------------|
| GET | /api/v1/inventory |
| POST | /api/v1/inventory |
| PUT | /api/v1/inventory/{id} |

---

## Order APIs

| Method | Endpoint |
|----------|------------------------------|
| POST | /api/v1/orders |
| GET | /api/v1/orders |
| GET | /api/v1/orders/{id} |

---

## Payment APIs

| Method | Endpoint |
|----------|------------------------------|
| POST | /api/v1/payments |
| GET | /api/v1/payments |
| GET | /api/v1/payments/{id} |

---

## Notification APIs

| Method | Endpoint |
|----------|------------------------------|
| GET | /api/v1/notifications |
| POST | /api/v1/notifications |

---

# 📸 Project Screenshots

## Architecture

```
screenshots/architecture.png
```

---

## Kubernetes Deployment

```
screenshots/kubernetes-pods.png
```

---

## Eureka Dashboard

```
screenshots/eureka-dashboard.png
```

---

## Jenkins Pipeline

```
screenshots/jenkins-pipeline.png
```

---

## SonarQube Dashboard

```
screenshots/sonarqube-dashboard.png
```

---

## Grafana Dashboard

```
screenshots/grafana-dashboard.png
```

---

## Prometheus

```
screenshots/prometheus-dashboard.png
```

---

## Alertmanager

```
screenshots/alertmanager-dashboard.png
```

---

# ☸ Kubernetes Resources

The project contains Kubernetes manifests for every microservice.

```
k8s/

├── api-gateway
├── config-server
├── eureka-server
├── inventory-service
├── notification-service
├── order-service
├── payment-service
├── product-service
└── user-service
```

Each folder contains

- deployment.yaml
- service.yaml

---

# 📊 Monitoring Dashboard

Prometheus collects metrics from

- Spring Boot Actuator
- JVM
- Node Exporter
- cAdvisor

Grafana visualizes

- CPU Usage

- Memory Usage

- JVM Memory

- Disk Usage

- Network Usage

- Container Metrics

- Service Health

---

# 🔔 Alertmanager

Configured alerts include

- High CPU Usage

- High Memory Usage

- Service Down

- Low Disk Space

- JVM Memory Threshold

- Application Health Failure

---

# 🧪 Testing

### User Registration

✅ Tested

---

### User Login

✅ Tested

---

### JWT Authentication

✅ Tested

---

### Product APIs

✅ Tested

---

### API Gateway Routing

✅ Tested

---

### Service Discovery

✅ Tested

---

### PostgreSQL Connectivity

✅ Tested

---

### Docker Deployment

✅ Tested

---

### Kubernetes Deployment

✅ Tested

---

### Prometheus Monitoring

✅ Tested

---

### Grafana Dashboard

✅ Tested

---

### Alertmanager

✅ Tested

---

# 📈 Project Highlights

✔ Enterprise Architecture

✔ Microservices

✔ Spring Security JWT

✔ Spring Cloud

✔ Config Server

✔ Eureka

✔ API Gateway

✔ Docker

✔ Kubernetes

✔ Jenkins CI/CD

✔ SonarQube

✔ Prometheus

✔ Grafana

✔ Alertmanager

✔ PostgreSQL

✔ Production Ready Design

---
# 🚀 Future Enhancements

The following features can be implemented in future versions of the project:

- Deploy on AWS EKS
- Helm Charts
- Horizontal Pod Autoscaler (HPA)
- Kubernetes Ingress Controller
- AWS Load Balancer Integration
- Redis Caching
- Apache Kafka Event Streaming
- Distributed Tracing using Zipkin / Jaeger
- ELK Stack Logging
- GitOps using ArgoCD
- Blue-Green Deployment
- Canary Deployment
- Multi-Cluster Kubernetes
- OAuth2 Authentication
- OpenAPI / Swagger Documentation
- Email Notification Integration
- Payment Gateway Integration (Stripe/Razorpay)
- Kubernetes Secrets Management
- HashiCorp Vault Integration

---

# 📚 Learning Outcomes

This project helped in understanding:

- Enterprise Microservices Architecture
- Spring Boot Development
- Spring Security with JWT
- Spring Cloud Config Server
- Eureka Service Discovery
- Spring Cloud Gateway
- REST API Design
- PostgreSQL Integration
- Docker Containerization
- Docker Compose
- Kubernetes Deployments
- Kubernetes Services
- Rolling Updates
- Config Management
- Jenkins CI/CD Pipeline
- SonarQube Code Quality
- Prometheus Monitoring
- Grafana Dashboards
- Alertmanager Configuration
- Linux Administration
- Git & GitHub Workflow

---

# 💼 Resume Highlights

This project demonstrates practical experience with:

- Enterprise Java Development
- Backend API Development
- Microservices Architecture
- Secure REST APIs
- JWT Authentication
- Docker
- Kubernetes
- CI/CD Pipeline
- Monitoring & Observability
- DevOps Best Practices
- Cloud-Native Application Development

---

# 🏆 Key Achievements

- Designed and developed an enterprise-grade microservices application.
- Implemented centralized configuration using Spring Cloud Config.
- Configured service discovery using Eureka Server.
- Built secure REST APIs using JWT Authentication.
- Developed an API Gateway for request routing.
- Containerized all microservices using Docker.
- Created a complete Docker Compose environment.
- Implemented Jenkins CI/CD pipeline.
- Integrated SonarQube for code quality analysis.
- Configured Prometheus and Grafana for monitoring.
- Implemented Alertmanager for alerting.
- Successfully migrated all microservices to Kubernetes.
- Performed end-to-end testing of all deployed services.

---

# 📊 Project Statistics

| Category | Count |
|----------|------:|
| Microservices | 6 |
| Infrastructure Services | 3 |
| Total Deployments | 9 |
| Kubernetes Services | 9 |
| Databases | 6 |
| API Gateway | 1 |
| Config Server | 1 |
| Eureka Server | 1 |
| Docker Images | 9 |
| Jenkins Pipeline | 1 |
| Monitoring Stack | 5 |

---

# 🤝 Contributing

Contributions are welcome.

If you would like to improve this project:

1. Fork the repository.
2. Create a new feature branch.
3. Commit your changes.
4. Push the branch.
5. Open a Pull Request.

---

# ⭐ Support

If you found this project helpful:

- ⭐ Star this repository
- 🍴 Fork the repository
- 💡 Share your feedback
- 🛠️ Suggest improvements

---

# 📄 License

This project is licensed under the MIT License.

You are free to use, modify, and distribute this project for educational and personal purposes.

---

# 🙏 Acknowledgements

Special thanks to the creators and maintainers of the following technologies:

- Java
- Spring Boot
- Spring Cloud
- Spring Security
- PostgreSQL
- Docker
- Kubernetes
- Jenkins
- SonarQube
- Prometheus
- Grafana
- Alertmanager
- Git & GitHub

---

# 👨‍💻 Author

**Pratap Kumar Sahoo**

Backend Java Developer | Spring Boot | Microservices | Docker | Kubernetes | DevOps

### Tech Stack

- Java
- Spring Boot
- Spring Cloud
- Spring Security
- PostgreSQL
- Docker
- Kubernetes
- Jenkins
- SonarQube
- Prometheus
- Grafana
- Alertmanager
- Git
- GitHub

---

# ⭐ If you like this project...

Please consider giving it a ⭐ on GitHub!

It motivates me to build more enterprise-level open-source projects.

---

<p align="center">

## 🚀 Thank You for Visiting!

**Happy Coding! ❤️**

</p>
