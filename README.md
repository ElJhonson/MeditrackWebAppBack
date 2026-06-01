# 💊 MediTrack API

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker)
![Deploy](https://img.shields.io/badge/Deploy-Render-46E3B7?logo=render)
![License](https://img.shields.io/badge/License-MIT-yellow)

REST API for medication reminder management. Built with Spring Boot 3, featuring JWT-based authentication, role-based access control, automated scheduling, and PostgreSQL persistence via Neon Serverless. Deployed on Render.

---

## 🚀 Features

- **JWT Authentication** — Stateless auth with access and refresh token support
- **Role-based Access Control** — Fine-grained permissions per user role
- **Medication Management** — Full CRUD for medications and schedules
- **Automated Reminders** — Scheduled jobs to trigger medication reminders
- **PostgreSQL via Neon** — Serverless, scalable database backend
- **Dockerized** — Ready to run in any container environment
- **CI/CD** — Automated pipeline via GitHub Actions

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Persistence | Spring Data JPA + PostgreSQL |
| Database | Neon Serverless PostgreSQL |
| Validation | Spring Boot Starter Validation |
| Utilities | Lombok |
| Containerization | Docker (eclipse-temurin:21-jdk-alpine) |
| Deploy | Render |
| Build | Maven |

---

## 📁 Project Structure

```
src/
└── main/
    └── java/com/meditrack/
        ├── config/         # Security, JWT, and app configuration
        ├── controller/     # REST controllers (Auth, Users, Medications, Reminders)
        ├── dto/            # Request and response DTOs
        ├── entity/         # JPA entities
        ├── exception/      # Global exception handling
        ├── repository/     # Spring Data JPA repositories
        ├── scheduler/      # Scheduled tasks for reminders
        └── service/        # Business logic layer
```

---

## ⚙️ Environment Variables

Create a `.env` file or set the following variables in your environment before running:

```env
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://<neon-host>/<db-name>?sslmode=require
SPRING_DATASOURCE_USERNAME=your_db_user
SPRING_DATASOURCE_PASSWORD=your_db_password

# JWT
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION_MS=86400000

# App
SERVER_PORT=8080
```

---

## 🏃 Running Locally

### Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL database (or a [Neon](https://neon.tech) serverless instance)

### With Maven

```bash
# Clone the repository
git clone https://github.com/LuisAlvarezMtz/meditrack-api.git
cd meditrack-api

# Set your environment variables, then run
./mvnw spring-boot:run
```

### With Docker

```bash
# Build the image
docker build -t meditrack-api .

# Run the container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=... \
  -e SPRING_DATASOURCE_USERNAME=... \
  -e SPRING_DATASOURCE_PASSWORD=... \
  -e JWT_SECRET=... \
  meditrack-api
```

The API will be available at `http://localhost:8080`.

---

## 📡 API Endpoints

### Auth

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/auth/register` | Register a new user | No |
| POST | `/api/auth/login` | Login and receive JWT | No |
| POST | `/api/auth/refresh` | Refresh access token | No |

### Users

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/users/me` | Get current user profile | Yes |
| PUT | `/api/users/me` | Update current user | Yes |

### Medications

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/medications` | List all medications | Yes |
| POST | `/api/medications` | Create a medication | Yes |
| GET | `/api/medications/{id}` | Get medication by ID | Yes |
| PUT | `/api/medications/{id}` | Update a medication | Yes |
| DELETE | `/api/medications/{id}` | Delete a medication | Yes |

### Reminders

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/reminders` | List all reminders | Yes |
| POST | `/api/reminders` | Create a reminder | Yes |
| PUT | `/api/reminders/{id}` | Update a reminder | Yes |
| DELETE | `/api/reminders/{id}` | Delete a reminder | Yes |

> All protected endpoints require the `Authorization: Bearer <token>` header.

---

## 🔐 Authentication Flow

```
1. POST /api/auth/register  →  Create account
2. POST /api/auth/login     →  Receive JWT access token
3. Use token in header      →  Authorization: Bearer <token>
```

---

## 🐳 Docker

The `Dockerfile` uses a multi-step approach with `eclipse-temurin:21-jdk-alpine` as the base image, builds the project with Maven Wrapper, and exposes port `8080`.

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/MediTrackApp-0.0.1-SNAPSHOT.jar"]
```

---

## 🔄 CI/CD

This project uses **GitHub Actions** for automated builds and deployments. The pipeline is defined in `.github/workflows/` and handles:

- Building and testing on every push to `main`
- Automatic deploy to Render on successful builds

---

## ☁️ Deployment

The API is deployed on **[Render](https://render.com)** and connected to a **[Neon](https://neon.tech)** serverless PostgreSQL database.

> 🔗 Live API: `https://your-app.onrender.com` *(update with your actual URL)*

---

## 🤝 Related Repositories

- [`meditrack-mobile`](https://github.com/LuisAlvarezMtz) — Mobile client *(link to your frontend repo)*

---

## 📄 License

This project is licensed under the MIT License.
