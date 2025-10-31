# 🚀 Kaddem CI/CD Deployment Project

![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-blue?logo=jenkins&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Containerization-2496ED?logo=docker&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.5-brightgreen?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-Build-orange?logo=apachemaven)
![License](https://img.shields.io/badge/license-Apache%202.0-lightgrey)

## 🧩 Overview

**Kaddem** is a **Spring Boot-based microservice** project designed for academic management.  
This repository includes the full backend source code, packaged and deployed through a **Jenkins CI/CD pipeline** using **Docker containers**.

The pipeline automates the build, test, and deployment stages — ensuring a reliable and scalable workflow.

---

## 🧱 Project Architecture

```
└── kaddem/
    ├── src/
    │   ├── main/java/tn/esprit/spring/kaddem/
    │   │   ├── controllers/       # REST API controllers
    │   │   ├── entities/          # JPA Entities
    │   │   ├── repositories/      # Spring Data JPA Repositories
    │   │   └── services/          # Business logic layer
    │   └── resources/             # Application configuration
    ├── pom.xml                    # Maven configuration
    ├── Dockerfile                 # Container definition
    ├── Jenkinsfile                # Jenkins CI/CD pipeline
    └── README.md                  # Project documentation
```

---

## ⚙️ CI/CD Pipeline (Jenkins)

### Pipeline Stages:
1. **Checkout Code** – Pull source code from GitHub repository.
2. **Build** – Compile and package the Spring Boot app with Maven.
3. **Test** – Run automated unit and integration tests.
4. **Dockerize** – Build Docker image for deployment.
5. **Push to Registry** – Upload Docker image to DockerHub or private registry.
6. **Deploy** – Run container on a target environment (e.g., staging or production).

### Example Jenkinsfile Snippet
```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps { git 'https://github.com/your-username/kaddem.git' }
        }
        stage('Build') {
            steps { sh 'mvn clean package -DskipTests' }
        }
        stage('Test') {
            steps { sh 'mvn test' }
        }
        stage('Docker Build & Push') {
            steps {
                sh 'docker build -t your-dockerhub-user/kaddem:latest .'
                sh 'docker push your-dockerhub-user/kaddem:latest'
            }
        }
        stage('Deploy') {
            steps { sh 'docker run -d -p 8089:8089 your-dockerhub-user/kaddem:latest' }
        }
    }
}
```

---

## 🐳 Docker Setup

### Build Image
```bash
docker build -t kaddem-app .
```

### Run Container
```bash
docker run -d -p 8089:8089 kaddem-app
```

Access the API via: **http://localhost:8089/Kaddem**

---

## 📦 Maven Commands

```bash
mvn clean install       # Build the project
mvn spring-boot:run     # Run the app locally
mvn test                # Run all unit tests
```

---

## 📜 API Documentation

After starting the application, visit:

👉 **Swagger UI** → [http://localhost:8089/swagger-ui.html](http://localhost:8089/swagger-ui.html)

---

## 📁 Environment Variables

| Variable | Description | Example |
|-----------|--------------|---------|
| `DB_HOST` | MySQL database host | `localhost` |
| `DB_PORT` | MySQL port | `3306` |
| `DB_USER` | Database username | `root` |
| `DB_PASS` | Database password | `root` |

---

## 🧑‍💻 Contributors

- **Chedli Khaddem** – Developer & DevOps Engineer

---

## 🛡 License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).

---

> 💡 _Automated builds. Seamless deployment. Continuous delivery._  
> _Designed for reliability, scalability, and simplicity._
