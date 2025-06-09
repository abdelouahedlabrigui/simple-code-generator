<<<<<<< HEAD
# Spring Code Generator

A project integrating **Django** (Python), **Spring Boot** (Java), and **PostgreSQL** using Docker to generate and manage code from datasets.

---

## 🛠️ Prerequisites

- Python 3.11+
- Java JDK 21
- Docker + Docker Compose
- PostgreSQL
- Git

---

## 📁 Project Setup

### 🐍 Django Backend (Python)

1. **Create and activate virtual environment:**
   ```bash
   mkdir spring-code-generator
   cd spring-code-generator/
   python3.11 -m venv codegen
   source codegen/bin/activate
   ```

2. **Install dependencies:**
   ```bash
   pip install Django djangorestframework requests pandas
   ```

3. **Create Django project and app:**
   ```bash
   django-admin startproject generator codegenerator
   cd codegenerator/
   python3.11 manage.py startapp processcode
   ```

4. **Apply migrations:**
   ```bash
   python3.11 manage.py makemigrations
   python3.11 manage.py migrate
   ```

5. **Run development server:**
   ```bash
   python3.11 manage.py runserver
   ```

---

### ☕ Spring Boot (Java)

1. **Generate Spring Boot Project** from [start.spring.io](https://start.spring.io/)
   - Recommended dependencies:
     - Spring Web
     - Spring Data JPA
     - PostgreSQL Driver

2. **Configure PostgreSQL in `application.properties`:**
   ```properties
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

3. **Run Spring Boot Application:**
   ```bash
   /usr/bin/env /usr/lib/jvm/jdk-21.0.7-oracle-x64/bin/java \
   @/tmp/cp_zyict3i0ybe6kuvogyq13o62.argfile demo.spring.project.ProjectApplication
   ```

---

### 🐘 PostgreSQL via Docker

1. Navigate to container directory:
   ```bash
   cd postgres-container/
   ```

2. Launch PostgreSQL container:
   ```bash
   sudo docker compose up -d
   ```

---

## 🔁 Simulation Workflow

### 📤 Route 1 – Submit Dataset for Code Generation

**URL:**
```
http://127.0.0.1:8000/processcode/submit-dataset/
```

**Example Input:**
- Dataset URL:
  ```
  https://raw.githubusercontent.com/lukes/ISO-3166-Countries-with-Regional-Codes/refs/heads/master/all/all.csv
  ```
- Local Path:
  ```
  /home/user1/Software/microservices/python-software/services/data/codes/country-code.csv
  ```
- Classname: `CountryCode`
- Packages String (comma-separated):
  ```
  package demo.spring.project.models;,package demo.spring.project.repositories;,package demo.spring.project.controllers;
  ```
- Addresses String (comma-separated paths):
  ```
  /home/user1/Software/microservices/repositories/spring-code-generator/project/src/main/java/demo/spring/project/models,
  /home/abdelouahedlabrigui/Software/microservices/repositories/spring-code-generator/project/src/main/java/demo/spring/project/repositories,
  /home/abdelouahedlabrigui/Software/microservices/repositories/spring-code-generator/project/src/main/java/demo/spring/project/controllers
  ```
- Table Name: `country_code`

---

### 📤 Route 2 – Submit Data to Database

**URL:**
```
http://127.0.0.1:8000/processcode/submit-post-dataset/
```

**CSV File Path:**
```
/home/user1/Software/microservices/python-software/services/data/codes/country-code.csv
```

**Spring Boot Endpoint:**
```
http://localhost:8081/api/country_code/add
```

---

## 🧪 Testing

1. Start Django:
   ```bash
   python3.11 manage.py runserver
   ```

2. Start Spring Boot:
   ```bash
   /usr/bin/env /usr/lib/jvm/jdk-21.0.7-oracle-x64/bin/java @/tmp/... ProjectApplication
   ```

3. Use Postman or CURL to test both endpoints above with the sample dataset.

---

## 📎 Notes

- Ensure your PostgreSQL container is running before launching Spring Boot.
- Confirm that dataset files are reachable from Django backend.
- Check logs for errors and update package paths or endpoint URLs accordingly.

---

## 📄 License

This project is for educational and internal use. Customize and extend as needed.

---
=======
# simple-code-generator
>>>>>>> 211a9f560adda22c9464bec04865d25d2e0dd70f
