# Jhatpat Services

A backend REST API application built with Java and Spring Boot, providing secure, scalable service management with JWT-based authentication and Redis-powered session management.

---

## 🚀 Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java |
| Framework | Spring Boot |
| Security | Spring Security + JWT |
| Database | MySQL + Spring Data JPA |
| Caching / Session | Redis + Spring Session |
| Containerization | Docker + Docker Compose |
| Build Tool | Maven |
| API Docs | Swagger / OpenAPI |

---

## 📦 Features

- **JWT Authentication** — Stateless, token-based login and authorization
- **Role-based Access Control** — Secured endpoints via Spring Security
- **Service Management APIs** — CRUD operations for core service entities
- **Redis Session Management** — Distributed session storage to reduce database load
- **Email Notifications** — Transactional emails via Spring Boot Mail
- **Swagger UI** — Interactive API documentation at `/swagger-ui/index.html`
- **Dockerized Deployment** — Full stack runs with a single `docker-compose up`

---

## 🛠️ Getting Started

### Prerequisites

- [Docker](https://www.docker.com/) and Docker Compose installed
- Java 17+ (for local development without Docker)
- Maven 3.x (for local development without Docker)

---

### Run with Docker (Recommended)

```bash
# Clone the repository
git clone <your-repo-url>
cd jhatpat-services

# Build and start all services
docker-compose up --build
```

The app will be available at: `http://localhost:8080`

To stop:
```bash
docker-compose down
```

---

### Run Locally (Without Docker)

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

> Make sure MySQL and Redis are running locally and configured in `application.properties`.

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/       # REST API controllers
│   │   ├── service/          # Business logic
│   │   ├── repository/       # Spring Data JPA repositories
│   │   ├── model/            # Entity classes
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── security/         # JWT + Spring Security config
│   │   └── config/           # App configuration (Redis, Mail, etc.)
│   └── resources/
│       └── application.properties
└── test/
```

> Note: Actual package structure may vary. Update this section to reflect your codebase.

---

## 📄 API Documentation

Once the app is running, visit:

```
http://localhost:8080/swagger-ui/index.html
```

All available endpoints are documented and testable via the Swagger UI.

---

## 🐳 Docker Services

Defined in `docker-compose.yml`:

| Service | Container Name | Port |
|---------|---------------|------|
| Spring Boot App | `springboot-app` | `8080` |
| Redis | `redis-server` | `6379` |

---

## ⚙️ Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_DATA_REDIS_HOST` | `redis` | Redis hostname |
| `SPRING_DATA_REDIS_PORT` | `6379` | Redis port |

> Additional variables like DB credentials and mail config should be set in `application.properties` or passed as environment variables.

---

## 🔐 Authentication Flow

1. Client sends `POST /api/auth/login` with credentials
2. Server validates and returns a **JWT token**
3. Client includes token in `Authorization: Bearer <token>` header
4. Spring Security validates token on every protected request

---

## 📬 Contact

Built by **[Your Name]**
- GitHub: [your-github]
- LinkedIn: [your-linkedin]
