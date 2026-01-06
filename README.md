
# Microservice Communication Task
This project implements a secure microservices architecture consisting of two Spring Boot applications and a PostgreSQL database, orchestrated via Docker Compose.

## Tech Stack
- Java 17 / Spring Boot 4.0.1
- Spring Security & JWT
- Spring Data JPA
- Docker & Docker Compose
- PostgreSQL 17

## Prerequisites
- Docker
- Maven

## Environment Variables
The application is configured via environment variables. You can modify them in the `docker-compose.yml` or create a `.env` file:

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `POSTGRES_DB` | `db` | Database name |
| `POSTGRES_URL` | `jdbc:postgresql://postgres:5432/db` | Database url |
| `POSTGRES_USER` | `test` | Database username |
| `POSTGRES_PASSWORD` | `password` | Database password |
| `INTERNAL_TOKEN` | `123321` | Shared secret between Service A and B |
| `JWT_SECRET` | `8fae5f...` | Secret key for signing JWT tokens |
| `SERVICES_DATA-API` | `http://data-api:8081/` | In docker data-api url |

## Getting Started
### 1. Build and Run via Docker
Execute the following command in the root directory to build and start all services:

```bash
docker compose up -d --build
```
## API Usage Flow

### Step 1: Register a new user
```bash
curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"email":"dev@example.com","password":"password123"}'
```

### Step 2: Login to obtain JWT
```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"email":"dev@example.com","password":"password123"}'
```
*Copy the token from the response.*

### Step 3: Process text (Protected Endpoint)
```bash
curl -X POST http://localhost:8080/api/process \
-H "Authorization: Bearer <YOUR_TOKEN>" \
-H "Content-Type: application/json" \
-d '{"text":"hello world"}'
```
