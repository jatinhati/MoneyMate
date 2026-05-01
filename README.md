# MoneyMate

MoneyMate is a personal finance manager web application (backend + frontend) for tracking incomes, expenses, categories and viewing dashboard summaries.

---

## Quick checklist
- [x] Read this README
- [x] Build and run backend locally with Maven
- [x] Run frontend with Vite
- [x] Build Docker image for backend
- [ ] Replace any committed secrets and use environment variables / secret manager

---

## Repository layout

- `Backend/` — Spring Boot backend (Java)
- `MoneyMate-Frontend/` — React frontend (Vite)
- `Backend/target/` — build artifacts (JAR) created by Maven

## Technology stack

- Backend
  - Java 21
  - Spring Boot 3.5.5
  - Spring Web, Spring Data JPA, Spring Security, Spring Mail, Spring Actuator
  - JWT (`io.jsonwebtoken` / jjwt)
  - MySQL (development) and PostgreSQL (production dependency present)
  - Apache POI, Lombok
  - Scheduling enabled (`@EnableScheduling`)

- Frontend
  - React 19, Vite
  - Tailwind related packages
  - axios, react-router-dom, react-hot-toast, recharts

## High-level architecture

- Layered Spring Boot application
  - Controllers: `controller/` — REST endpoints
  - Services: `service/` — business logic
  - Repositories: `repositories/` — JPA repositories
  - Entities & DTOs: `entity/` and `dto/`
  - Security: `config/SecurityConfig.java`, `security/JwtRequestFilter`

## Important files

- `Backend/pom.xml` — backend dependencies and Java version
- `Backend/src/main/resources/application.properties` — development configuration
- `Backend/src/main/resources/application-prod.properties` — production configuration
- `Backend/Dockerfile` — Docker image build for backend
- Frontend entry: `MoneyMate-Frontend/package.json` and `MoneyMate-Frontend/src/`

## Environment variables and configuration

The application references the following environment variables (use your secret manager or platform env vars in production):

- `BREVO_USERNAME` — SMTP username for Brevo
- `BREVO_PASSWORD` — SMTP password for Brevo
- `BREVO_MAIL` — sender email used by SMTP and PB
- `BREVO_API_KEY` — Brevo HTTP API key (fallback)
- `MONEYMATE_BACKEND_URL` — backend base URL used for activation links
- `MONEYMATE_FRONTEND` — frontend base URL used in emails

Spring property keys of interest:
- `spring.datasource.*` — DB url/username/password/driver
- `jwt.secret` and `jwt.expiration` — JWT configuration (present in `application.properties`) 
- `server.servlet.context-path` — REST context path (`/api/v1.0`)

Important: Several secrets are currently present in repository properties files (`application.properties`, `application-prod.properties`). Move these to secure storage and rotate them.

## Databases

- Local / dev: MySQL configured in `application.properties` (`jdbc:mysql://localhost:3306/moneymanager`).
- Production: PostgreSQL connection shown in `application-prod.properties` (example Render DB). Replace and secure credentials.

## Running the backend (development)

From the backend folder:

```bash
cd Backend
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

Or run the packaged jar:

```bash
java -jar Backend/target/MoneyMate-0.0.1-SNAPSHOT.jar
```

Default API context: http://localhost:8080/api/v1.0/

## Running the frontend (development)

From the frontend folder:

```bash
cd MoneyMate-Frontend
npm install
npm run dev
```

Vite serves the app (typically on port 5173). Ensure frontend uses the correct backend base URL (with `/api/v1.0` context path).

## Docker (backend)

`Backend/Dockerfile` uses Eclipse Temurin JRE 21 and expects the backend jar at `Backend/target/MoneyMate-0.0.1-SNAPSHOT.jar`.

Build image (from repo root):

```bash
docker build -t moneymate-backend .
```

Run container with environment variables (example):

```bash
docker run -p 7777:7777 \
  -e BREVO_API_KEY="your_brevo_api_key" \
  -e BREVO_MAIL="no-reply@example.com" \
  -e MONEYMATE_BACKEND_URL="https://api.example.com" \
  -e MONEYMATE_FRONTEND="https://app.example.com" \
  moneymate-backend
```

Note: Docker ENTRYPOINT sets `-Dspring.profiles.active=prod` — production properties will be used by the container.

## API summary

Public endpoints (no auth):
- `POST /register` — register
- `POST /login` — login (returns JWT)
- `GET /activate?token=...` — account activation
- `GET /status` or `/health` — healthcheck
- `GET /test` — test endpoint

Authenticated endpoints (require JWT):
- `GET /dashboard`
- `POST/GET /categories`, `GET /categories/{type}`, `PUT /categories/{categoryId}`
- `POST/GET/DELETE /expenses` (`DELETE /expenses/{id}`)
- `POST/GET/DELETE /incomes` (`DELETE /incomes/{id}`)
- `POST /filter` — filtering; verify mapping on `FilterController`

Inspect `Backend/src/main/java/org/example/MoneyManager/dto/` for request/response shapes.

## Security & hardening recommendations

1. Remove secrets from repo and use environment variables or a secrets manager (Rotate credentials now).
2. Restrict CORS origins (currently `*` in `SecurityConfig`). Use frontend origin(s) in production.
3. Secure actuator endpoints and health checks.
4. Ensure JWT secret is provided securely (do not commit long secrets to VCS).

## Troubleshooting

- Backend startup failures: check DB connectivity and `spring.datasource` values.
- Email sending issues: configure `BREVO_*` vars or confirm SMTP availability; app has HTTP API fallback.
- Frontend network errors: confirm frontend uses the correct backend base url and your browser is allowed to access it (CORS).

## Recommended next steps

1. Replace committed secrets and add `.env.example` documenting required env vars.
2. Add CI pipeline to build backend and frontend and to publish Docker images.
3. Lock down CORS and actuator endpoints for production.
4. Create API docs (Swagger/OpenAPI) or extract a comprehensive endpoint/DTO list.

## Where to look in the code

- `Backend/pom.xml`
- `Backend/src/main/resources/application.properties`
- `Backend/src/main/resources/application-prod.properties`
- `Backend/Dockerfile`
- `Backend/src/main/java/org/example/MoneyManager/config/SecurityConfig.java`
- `MoneyMate-Frontend/src/` (frontend code)

---

Generated on May 1, 2026. Review and update as implementation details evolve.
