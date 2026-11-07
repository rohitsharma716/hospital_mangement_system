# 🏥 Hospital Management System

A Spring Boot-based REST API for managing hospital operations including patient records, appointments, departments, doctors, and insurance, secured with **JWT Authentication** and **OAuth2 Social Logins (Google & GitHub)**.

---

## 🚀 Tech Stack

- **Java**: 21
- **Framework**: Spring Boot 4.1.1
- **Security**: Spring Security 7.x, JJWT (0.12.6), Spring OAuth2 Client
- **Database**: PostgreSQL
- **ORM / Persistence**: Spring Data JPA, Hibernate ORM 7.x
- **Build Tool**: Maven

---

## 🔐 Authentication & Authorization Architecture

The application supports dual authentication mechanisms, both culminating in a signed **JWT Bearer Token**:

1. **Classic Authentication**:
   - `POST /auth/signup` - Register standard user (defaults to `ROLE_PATIENT`).
   - `POST /auth/login` - Authenticate with username and password.

2. **OAuth2 Social Logins**:
   - **GitHub**: Login via `/oauth2/authorization/github`
   - **Google**: Login via `/oauth2/authorization/google`
   - Automatic account creation with `role = PATIENT`, `password = NULL`, and linked `provider_id` + `provider_type`.
   - Built-in account hijacking protection (blocks OAuth2 registration if a local username/email already exists).

### 👥 Role-Based Access Control (RBAC)

| Role | Accessible Endpoints |
|---|---|
| **Public / Any** | `/auth/**`, `/public/**`, `/oauth2/**`, `/login/oauth2/**` |
| **PATIENT** | `/patients/**`, `/appointments/**`, `/admit/**` |
| **DOCTOR** | `/doctors/**`, `/hospital/**`, `/departments/**`, `/patients/**`, `/appointments/**` |
| **ADMIN** | Full access (`/admin/**`, `/doctors/**`, `/hospital/**`, `/departments/**`, `/patients/**`, `/appointments/**`) |

---

## ⚙️ Configuration & Prerequisites

### 1. Database Setup (PostgreSQL)

Create a database named `mydb`:
```sql
CREATE DATABASE mydb;
```

Update your database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=rohit
spring.datasource.password=your_password
```

### 2. OAuth2 Setup

#### **GitHub OAuth Setup**
1. Navigate to: **GitHub Settings** ➔ **Developer Settings** ➔ **OAuth Apps** ➔ **New OAuth App**.
2. Fill in the parameters:
   - **Application Name**: `Hospital Management`
   - **Homepage URL**: `http://localhost:8080`
   - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
3. Generate client secret and update `application.properties`:
```properties
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
spring.security.oauth2.client.registration.github.scope=read:user,user:email
```

#### **Google OAuth Setup**
1. Navigate to: **Google Cloud Console** ➔ **APIs & Services** ➔ **Credentials**.
2. Create an **OAuth 2.0 Client ID** (Web Application).
3. Add Authorized redirect URI:
   - `http://localhost:8080/login/oauth2/code/google`
4. Update `application.properties`:
```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=openid,profile,email
```

---

## 🏃 Running the Application

### 1. Build and Compile
```bash
./mvnw clean compile
```

### 2. Start the Server
```bash
./mvnw spring-boot:run
```
The application will launch on `http://localhost:8080`.

---

## 🧪 Testing the APIs

### 1. Classic Signup / Login

**Sign Up:**
```bash
curl -X POST http://localhost:8080/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "secretpassword",
    "role": "PATIENT"
  }'
```

**Log In:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "secretpassword"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "john_doe",
  "role": "PATIENT"
}
```

### 2. Test OAuth2 in Browser

- **GitHub Login**: Visit `http://localhost:8080/oauth2/authorization/github`
- **Google Login**: Visit `http://localhost:8080/oauth2/authorization/google`

Upon successful authorization, the server responds directly with your JWT JSON payload:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "your_github_username",
  "role": "PATIENT"
}
```

### 3. Access Protected Endpoints

Pass the returned JWT in the `Authorization` header:
```bash
curl -X GET http://localhost:8080/patients/ \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

---

## 📁 Project Structure

```
src/main/java/com/rohit/hospitalMangement/
├── config/              # Application bean configurations
├── controller/          # REST Controllers (Auth, Patient, Doctor, Appointment, etc.)
├── DTO/                 # Data Transfer Objects (LoginRequest, SignUpResponse, etc.)
├── Entity/              # JPA Entities (Users, Patient, Doctor, Appointment, Department, Insurance)
│   └── type/            # Enums (BloodGroup, ProviderType)
├── repository/          # Spring Data JPA Repositories
├── security/            # Security Filters, OAuth2 Handler, Auth Utils & Services
│   ├── AuthService.java
│   ├── AuthUtils.java
│   ├── JwtAuthenticationFilter.java
│   ├── OAuth2SuccessHandler.java
│   └── WebSecurityConfig.java
└── service/             # Business logic layer
```

---

## 🛡️ Pre-seeded Test Users (`data.sql`)

When the application boots with database initialization active, standard test accounts are provisioned:

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | `ROLE_ADMIN` |
| `doctor` | `doctor123` | `ROLE_DOCTOR` |
| `patient` | `patient123` | `ROLE_PATIENT` |
