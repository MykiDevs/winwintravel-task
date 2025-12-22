
# Microservices Communication Task: Auth-Service & Data-API

This project implements a secure microservices architecture consisting of two Spring Boot applications and a PostgreSQL database, orchestrated via Docker Compose.

## Architecture Overview

1.  **Auth-Service (Service A):** Handles user registration, authentication (JWT), and processing logs. It acts as a gateway that communicates with the Data-API.
2.  **Data-API (Service B):** A protected service that performs text transformation. It only accepts requests verified by a shared `X-Internal-Token`.
3.  **PostgreSQL:** Stores persistent data for users and processing history.

## Technologies
- Java 17 / Spring Boot 4.0.1
- Spring Security & JWT
- Spring Data JPA
- Docker & Docker Compose
- PostgreSQL 17

## Prerequisites
- Docker (Desktop or Engine)
- Maven (optional, for manual builds)

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

### 1. Build and Run
Execute the following command in the root directory to build and start all services:

```bash
docker compose up -d --build
```

The system uses a **Healthcheck** mechanism. The services will wait for the PostgreSQL container to be fully initialized before starting.

### 2. Service Access
- **Auth-Service:** `http://localhost:8080`
- **Data-API:** `http://localhost:8081`

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

**Expected Result:**
1. Service A validates your JWT.
2. Service A sends a request to Service B with `X-Internal-Token`.
3. Service B returns uppercase text.
4. Service A saves the log to PostgreSQL and returns the result to you.

## Database Schema
The system automatically manages two tables:
- `users`: Stores credentials (passwords are hashed via BCrypt).
- `processing_log`: Stores interaction history (User ID, input, output, and timestamp).