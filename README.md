# 🧠 Smart Job Application Tracker — Backend

A production-ready REST API built with **Spring Boot 3**, **Spring Security**, and **JWT Authentication**, connected to **PostgreSQL (Supabase)** and deployed on **Railway**.

---

## 🚀 Live API

```
https://job-tracker-backend-production-b07b.up.railway.app/api
```

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| Java 21 | Programming language |
| Spring Boot 3.2.3 | Backend framework |
| Spring Security | Authentication & authorization |
| JWT (JSON Web Token) | Stateless auth tokens |
| Spring Data JPA | ORM / database access |
| Hibernate | Entity management |
| PostgreSQL | Production database (Supabase) |
| HikariCP | Connection pooling |
| Maven | Build tool |
| Docker | Containerization |
| Railway | Cloud deployment |

---

## 📁 Project Structure

```
src/main/java/com/jobtracker/
├── config/
│   ├── SecurityConfig.java       # JWT + CORS configuration
│   └── CorsFilter.java           # Global CORS filter
├── controller/
│   ├── AuthController.java       # Register & Login endpoints
│   └── JobController.java        # CRUD + Analytics endpoints
├── model/
│   ├── User.java                 # User entity
│   └── JobApplication.java       # Job application entity
├── repository/
│   ├── UserRepository.java       # User DB operations
│   └── JobApplicationRepository.java
├── service/
│   ├── AuthService.java          # Auth business logic
│   ├── JobService.java           # Job business logic
│   └── UserDetailsServiceImpl.java
├── security/
│   ├── JwtAuthFilter.java        # JWT request filter
│   └── JwtUtil.java              # JWT token utilities
└── SmartJobApplicationTrackerApplication.java
```

---

## 🔌 API Endpoints

### Auth
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/auth/register` | Register new user | ❌ |
| POST | `/api/auth/login` | Login & get JWT token | ❌ |

### Jobs
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/jobs` | Get all user's jobs | ✅ |
| POST | `/api/jobs` | Add new job application | ✅ |
| PUT | `/api/jobs/{id}` | Update job application | ✅ |
| DELETE | `/api/jobs/{id}` | Delete job application | ✅ |
| GET | `/api/jobs/analytics` | Get analytics data | ✅ |

---

## 📦 Request & Response Examples

### Register
```json
POST /api/auth/register
{
  "name": "Ganesh Kumar",
  "email": "ganesh@example.com",
  "password": "password123"
}
```

### Login
```json
POST /api/auth/login
{
  "email": "ganesh@example.com",
  "password": "password123"
}

// Response
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "name": "Ganesh Kumar",
  "email": "ganesh@example.com"
}
```

### Add Job
```json
POST /api/jobs
Authorization: Bearer <token>
{
  "company": "Google",
  "role": "Software Engineer",
  "status": "APPLIED",
  "appliedDate": "2026-03-23",
  "jobLink": "https://careers.google.com",
  "notes": "Applied via referral"
}
```

### Analytics Response
```json
GET /api/jobs/analytics
{
  "totalApplications": 25,
  "successRate": 8.0,
  "statusBreakdown": {
    "WISHLIST": 3,
    "APPLIED": 12,
    "INTERVIEW": 6,
    "OFFER": 2,
    "REJECTED": 2
  },
  "weeklyApplications": [
    { "week": 10, "count": 5 },
    { "week": 11, "count": 8 }
  ]
}
```

---

## ⚙️ Job Status Types

```
WISHLIST → APPLIED → INTERVIEW → OFFER
                              ↘ REJECTED
```

---

## 🔧 Local Setup

### Prerequisites
- Java 21+
- Maven 3.9+
- MySQL (for local) or PostgreSQL

### 1. Clone the repository
```bash
git clone https://github.com/ghanigitHub06/job-tracker-backend.git
cd job-tracker-backend
```

### 2. Configure `application.properties`
```properties
# Local MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/job_tracker
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

app.jwt.secret=your-secret-key
app.jwt.expiration=86400000
server.port=8080
```

### 3. Run the application
```bash
mvn spring-boot:run
```

App starts at: `http://localhost:8080`

---

## 🐳 Docker

```dockerfile
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t job-tracker-backend .
docker run -p 8080:8080 job-tracker-backend
```

---

## ☁️ Deployment (Railway)

### Environment Variables on Railway
| Variable | Value |
|---|---|
| `DATABASE_URL` | `jdbc:postgresql://pooler-host:5432/postgres?sslmode=require` |
| `DATABASE_USERNAME` | `postgres.your-project-ref` |
| `DATABASE_PASSWORD` | `your-password` |
| `JWT_SECRET` | `your-jwt-secret` |
| `PORT` | `8080` |

---

## 🗄️ Database Schema

### users
| Column | Type | Description |
|---|---|---|
| id | BIGINT (PK) | Auto-generated |
| name | VARCHAR | User's full name |
| email | VARCHAR (unique) | User's email |
| password | VARCHAR | BCrypt hashed |

### job_applications
| Column | Type | Description |
|---|---|---|
| id | BIGINT (PK) | Auto-generated |
| company | VARCHAR | Company name |
| role | VARCHAR | Job role/title |
| status | ENUM | WISHLIST/APPLIED/INTERVIEW/OFFER/REJECTED |
| applied_date | DATE | Date applied |
| job_link | VARCHAR | Job posting URL |
| notes | TEXT | Personal notes |
| user_id | BIGINT (FK) | References users.id |
| created_at | TIMESTAMP | Auto-generated |

---

## 🔐 Security

- Passwords hashed with **BCrypt**
- JWT tokens expire in **24 hours**
- All `/api/jobs/**` endpoints require valid JWT
- CORS configured for Vercel frontend
- Stateless sessions (no server-side sessions)

---

## 👨‍💻 Author

**Ganesh Kumar Tirunalla**
- GitHub: [@ghanigitHub06](https://github.com/ghanigitHub06)

---

## 📄 License

MIT License — feel free to use this project for learning and portfolio purposes.
