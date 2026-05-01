# MoneyMate 💰

**MoneyMate** is a full-stack personal finance management web application that helps you track your income, expenses, and spending habits — all in one place. It features a Spring Boot REST API backend, a React frontend with interactive charts, JWT-based authentication, email notifications, and scheduled daily reminders.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Backend (Development)](#backend-development)
  - [Frontend (Development)](#frontend-development)
  - [Docker (Backend)](#docker-backend)
- [Environment Variables](#environment-variables)
- [API Reference](#api-reference)
  - [Authentication](#authentication)
  - [Dashboard](#dashboard)
  - [Categories](#categories)
  - [Expenses](#expenses)
  - [Incomes](#incomes)
  - [Filter](#filter)
  - [Health Check](#health-check)
- [Database](#database)
- [Scheduled Jobs](#scheduled-jobs)
- [Frontend Pages](#frontend-pages)
- [Security Notes](#security-notes)
- [Troubleshooting](#troubleshooting)
- [Recommended Next Steps](#recommended-next-steps)

---

## Features

- **User Authentication** — Register, email-based account activation, and JWT login
- **Dashboard** — View total balance, total income, total expenses, and recent transactions at a glance
- **Expense Tracking** — Add, view, and delete expenses for the current month, grouped by category
- **Income Tracking** — Add, view, and delete income entries for the current month
- **Category Management** — Create and manage custom income/expense categories with emoji icons
- **Transaction Filtering** — Filter income or expense records by date range, keyword, and sort order
- **Interactive Charts** — Visualize spending with Recharts-powered graphs on the frontend
- **Email Notifications** — Account activation email on registration, powered by Brevo (SMTP + HTTP API fallback)
- **Scheduled Reminders** — Automated daily email reminders to log expenses (10 PM IST) and daily expense summaries (11 PM IST)
- **Multi-database support** — MySQL for local development, PostgreSQL for production

---

## Tech Stack

### Backend
| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language |
| Spring Boot | 3.5.5 | Application framework |
| Spring Web | — | REST API |
| Spring Data JPA | — | Database ORM |
| Spring Security | — | Authentication & authorization |
| Spring Mail | — | Email via SMTP |
| Spring Actuator | — | Health and metrics endpoints |
| jjwt (io.jsonwebtoken) | 0.11.5 | JWT generation and validation |
| MySQL Connector | — | MySQL driver (dev) |
| PostgreSQL | — | PostgreSQL driver (prod) |
| Apache POI | 5.4.0 | Excel/Office file support |
| Lombok | — | Boilerplate reduction |
| BCryptPasswordEncoder | — | Password hashing |

### Frontend
| Technology | Version | Purpose |
|---|---|---|
| React | 19 | UI framework |
| Vite | 7 | Build tool and dev server |
| Tailwind CSS | 4 | Utility-first styling |
| React Router DOM | 7 | Client-side routing |
| Axios | — | HTTP requests to backend |
| Recharts | 3 | Charts and data visualization |
| Framer Motion | 12 | Animations |
| Lucide React | — | Icon library |
| emoji-picker-react | 4 | Emoji picker for categories |
| Moment.js | — | Date formatting |
| React Hot Toast | 2 | Toast notifications |

---

## Project Structure

```
MoneyMate/
├── Backend/                          # Spring Boot application
│   ├── Dockerfile                    # Docker image definition
│   ├── pom.xml                       # Maven dependencies
│   ├── mvnw / mvnw.cmd               # Maven wrapper scripts
│   └── src/
│       └── main/
│           ├── java/org/example/MoneyManager/
│           │   ├── MoneyManagerApplication.java   # Entry point
│           │   ├── config/
│           │   │   └── SecurityConfig.java        # Security + CORS + JWT filter chain
│           │   ├── controller/
│           │   │   ├── ProfileController.java     # /register, /login, /activate, /test
│           │   │   ├── DashboardController.java   # /dashboard
│           │   │   ├── CategoryController.java    # /categories
│           │   │   ├── ExpenseController.java     # /expenses
│           │   │   ├── IncomeController.java      # /incomes
│           │   │   ├── FilterController.java      # /filter
│           │   │   └── HomeController.java        # /status, /health
│           │   ├── service/
│           │   │   ├── ProfileService.java
│           │   │   ├── DashboardService.java
│           │   │   ├── CategoryService.java
│           │   │   ├── ExpenseService.java
│           │   │   ├── IncomeService.java
│           │   │   ├── EmailService.java
│           │   │   ├── BrevoEmailClient.java      # Brevo HTTP API fallback
│           │   │   ├── NotificationService.java   # Scheduled email jobs
│           │   │   └── AppUserDetailsService.java
│           │   ├── security/
│           │   │   └── JwtRequestFilter.java      # JWT token validation per request
│           │   ├── entity/
│           │   │   ├── ProfileEntity.java         # tbl_profiles
│           │   │   ├── CategoryEntity.java        # tbl_categories
│           │   │   ├── ExpenseEntity.java         # tbl_expense
│           │   │   └── IncomeEntity.java
│           │   ├── dto/
│           │   │   ├── AuthDTO.java
│           │   │   ├── ProfileDTO.java
│           │   │   ├── CategoryDTO.java
│           │   │   ├── ExpenseDTO.java
│           │   │   ├── IncomeDTO.java
│           │   │   ├── FilterDTO.java
│           │   │   └── RecentTransactionDTO.java
│           │   ├── repositories/                  # JPA repositories
│           │   └── util/
│           │       └── JwtUtil.java               # JWT helper
│           └── resources/
│               ├── application.properties         # Dev config (MySQL)
│               └── application-prod.properties    # Prod config (PostgreSQL)
│
└── MoneyMate-Frontend/               # React application
    ├── index.html
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── App.jsx                   # Router setup
        ├── main.jsx                  # React entry point
        └── pages/
            ├── LandingPage.jsx       # Public landing page
            ├── Login.jsx             # Login form
            ├── Signup.jsx            # Registration form
            ├── Home.jsx              # Dashboard
            ├── Expense.jsx           # Expense management
            ├── Income.jsx            # Income management
            ├── Category.jsx          # Category management
            └── Filter.jsx            # Transaction filtering
```

---

## Architecture

```
Browser
  │
  ▼
React Frontend (Vite, port 5173)
  │  HTTP requests with JWT bearer token
  ▼
Spring Boot REST API (port 8080, context: /api/v1.0)
  │
  ├── JwtRequestFilter  ← validates JWT on every authenticated request
  ├── SecurityConfig    ← defines public vs protected routes
  │
  ├── Controllers       ← handle HTTP routes, delegate to Services
  ├── Services          ← business logic, email, scheduling
  ├── Repositories      ← Spring Data JPA (auto-generated SQL)
  │
  └── Database
        ├── MySQL       (local development)
        └── PostgreSQL  (production, e.g. Render)
```

**Auth flow:**
1. User registers → activation email sent via Brevo
2. User clicks activation link → account activated
3. User logs in → server returns a JWT (valid for 1 hour by default)
4. Frontend stores JWT → sends it as `Authorization: Bearer <token>` on every request
5. `JwtRequestFilter` validates token and sets the Spring Security context

---

## Prerequisites

Before running MoneyMate locally, ensure you have:

- **Java 21** (JDK) — [Download](https://adoptium.net/)
- **Maven** (or use the included `./mvnw` wrapper)
- **MySQL 8+** — running locally with a database named `moneymanager`
- **Node.js 18+** and **npm** — for the frontend
- **Docker** (optional) — for containerized backend deployment
- A **Brevo** account for email (free tier available at [brevo.com](https://www.brevo.com))

---

## Getting Started

### Backend (Development)

**1. Configure your local database**

Create a MySQL database:
```sql
CREATE DATABASE moneymanager;
```

Update credentials in `Backend/src/main/resources/application.properties` if your MySQL user/password differs from the defaults.

**2. Set required environment variables**

```bash
export BREVO_USERNAME="your-brevo-smtp-username"
export BREVO_PASSWORD="your-brevo-smtp-password"
export BREVO_MAIL="no-reply@yourdomain.com"
export BREVO_API_KEY="your-brevo-api-key"
export MONEYMATE_BACKEND_URL="http://localhost:8080"
export MONEYMATE_FRONTEND="http://localhost:5173"
```

**3. Build and run**

```bash
cd Backend
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

The API will be available at: **http://localhost:8080/api/v1.0/**

> **Note:** `application.properties` sets `spring.profiles.active=prod` by default. Override it for local MySQL:
> ```bash
> ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
> ```
> Or comment out the `spring.profiles.active=prod` line in `application.properties`.

### Frontend (Development)

```bash
cd MoneyMate-Frontend
npm install
npm run dev
```

The app will be available at: **http://localhost:5173**

Make sure the frontend points to your backend URL. Update the base URL in the Axios configuration if it differs from the default.

### Docker (Backend)

The `Dockerfile` (in the repo root) uses Eclipse Temurin JRE 21 and runs the app with the `prod` Spring profile (PostgreSQL).

**1. Build the JAR first:**
```bash
cd Backend
./mvnw clean package -DskipTests
cd ..
```

**2. Build the Docker image:**
```bash
docker build -t moneymate-backend .
```

**3. Run the container:**
```bash
docker run -p 7777:7777 \
  -e BREVO_API_KEY="your_brevo_api_key" \
  -e BREVO_MAIL="no-reply@example.com" \
  -e MONEYMATE_BACKEND_URL="https://api.example.com" \
  -e MONEYMATE_FRONTEND="https://app.example.com" \
  moneymate-backend
```

The containerized API will be available at: **http://localhost:7777/api/v1.0/**

---

## Environment Variables

| Variable | Required | Description |
|---|---|---|
| `BREVO_USERNAME` | Yes | SMTP username for Brevo email relay |
| `BREVO_PASSWORD` | Yes | SMTP password for Brevo email relay |
| `BREVO_MAIL` | Yes | Sender email address shown to recipients |
| `BREVO_API_KEY` | Yes | Brevo HTTP API key (used as SMTP fallback) |
| `MONEYMATE_BACKEND_URL` | Yes | Backend base URL (used in activation email links) |
| `MONEYMATE_FRONTEND` | Yes | Frontend base URL (used in reminder email links) |

**Spring property overrides** (via env or `-D` flags):
- `spring.datasource.url` / `spring.datasource.username` / `spring.datasource.password`
- `jwt.secret` — JWT signing key (keep this long and secret)
- `jwt.expiration` — JWT validity in milliseconds (default: `3600000` = 1 hour)
- `server.servlet.context-path` — REST base path (default: `/api/v1.0`)

> ⚠️ **Security Warning:** `application.properties` and `application-prod.properties` currently contain hardcoded credentials. These must be moved to environment variables or a secrets manager and the credentials rotated before any production deployment.

---

## API Reference

All endpoints are prefixed with `/api/v1.0`. Authenticated endpoints require the header:
```
Authorization: Bearer <jwt_token>
```

### Authentication

#### Register a new user
```
POST /register
```
**Request body:**
```json
{
  "fullName": "Jane Doe",
  "email": "jane@example.com",
  "password": "securePassword123",
  "profileImageUrl": "https://example.com/avatar.png"
}
```
**Response `201 Created`:**
```json
{
  "id": 1,
  "fullName": "Jane Doe",
  "email": "jane@example.com",
  "profileImageUrl": "https://example.com/avatar.png",
  "createdAt": "2026-05-01T10:00:00",
  "updatedAt": "2026-05-01T10:00:00"
}
```
An activation email is sent to the registered address.

---

#### Activate account
```
GET /activate?token=<activation_token>
```
**Response `200 OK`:**
```
Profile activated successfully.
```
**Response `400 Bad Request`:**
```
Invalid activation token.
```

---

#### Login
```
POST /login
```
**Request body:**
```json
{
  "email": "jane@example.com",
  "password": "securePassword123"
}
```
**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "User": {
    "id": 1,
    "fullName": "Jane Doe",
    "email": "jane@example.com",
    "profileImageUrl": "https://example.com/avatar.png",
    "createdAt": "2026-05-01T10:00:00",
    "updatedAt": "2026-05-01T10:00:00"
  }
}
```
**Response `403 Forbidden`** if account is not yet activated.

---

### Dashboard

#### Get dashboard summary
```
GET /dashboard
```
🔒 Requires authentication.

**Response `200 OK`:**
```json
{
  "total balance": 1500.00,
  "total incomes": 3000.00,
  "total expenses": 1500.00,
  "recent 5 expenses": [ ...ExpenseDTO... ],
  "recent 5 incomes": [ ...IncomeDTO... ],
  "recent transactions": [
    {
      "id": 3,
      "profileId": 1,
      "icon": "🍔",
      "name": "Lunch",
      "amount": 12.50,
      "date": "2026-05-01",
      "type": "expense",
      "createdAt": "2026-05-01T13:00:00",
      "updatedAt": "2026-05-01T13:00:00"
    }
  ]
}
```
Recent transactions are sorted by date (newest first), then by `createdAt`.

---

### Categories

#### Create a category
```
POST /categories
```
🔒 Requires authentication.

**Request body:**
```json
{
  "name": "Food",
  "icon": "🍔",
  "type": "expense"
}
```
`type` must be `"income"` or `"expense"`.

**Response `201 Created`:**
```json
{
  "id": 5,
  "profileId": 1,
  "name": "Food",
  "icon": "🍔",
  "type": "expense",
  "createdAt": "2026-05-01T10:00:00",
  "updatedAt": "2026-05-01T10:00:00"
}
```

---

#### Get all categories for current user
```
GET /categories
```
🔒 Requires authentication.

**Response `200 OK`:** Array of `CategoryDTO`

---

#### Get categories by type
```
GET /categories/{type}
```
🔒 Requires authentication. `{type}` is `income` or `expense`.

**Response `200 OK`:** Array of `CategoryDTO` filtered by type.

---

#### Update a category
```
PUT /categories/{categoryId}
```
🔒 Requires authentication.

**Request body:** `CategoryDTO` fields to update (e.g., `name`, `icon`)

**Response `200 OK`:** Updated `CategoryDTO`

---

### Expenses

#### Add an expense
```
POST /expenses
```
🔒 Requires authentication.

**Request body:**
```json
{
  "name": "Grocery shopping",
  "icon": "🛒",
  "categoryId": 5,
  "amount": 85.50,
  "date": "2026-05-01"
}
```
`date` defaults to today if not provided.

**Response `201 Created`:** `ExpenseDTO`

---

#### Get current month's expenses
```
GET /expenses
```
🔒 Requires authentication.

**Response `200 OK`:** Array of `ExpenseDTO` for the current calendar month.

---

#### Delete an expense
```
DELETE /expenses/{id}
```
🔒 Requires authentication.

**Response `204 No Content`**

---

### Incomes

#### Add an income
```
POST /incomes
```
🔒 Requires authentication.

**Request body:**
```json
{
  "name": "Salary",
  "icon": "💼",
  "categoryId": 2,
  "amount": 3000.00,
  "date": "2026-05-01"
}
```

**Response `201 Created`:** `IncomeDTO`

---

#### Get current month's incomes
```
GET /incomes
```
🔒 Requires authentication.

**Response `200 OK`:** Array of `IncomeDTO` for the current calendar month.

---

#### Delete an income
```
DELETE /incomes/{id}
```
🔒 Requires authentication.

**Response `204 No Content`**

---

### Filter

#### Filter transactions
```
POST /filter
```
🔒 Requires authentication.

**Request body:**
```json
{
  "type": "expense",
  "startDate": "2026-04-01",
  "endDate": "2026-04-30",
  "keyword": "food",
  "sortField": "date",
  "sortOrder": "desc"
}
```
- `type`: `"income"` or `"expense"` (required)
- `startDate` / `endDate`: ISO date strings (optional, defaults to all-time / today)
- `keyword`: partial match on name (optional)
- `sortField`: field to sort by (optional, default `"date"`)
- `sortOrder`: `"asc"` or `"desc"` (optional, default `"asc"`)

**Response `200 OK`:** Array of `IncomeDTO` or `ExpenseDTO` depending on `type`

---

### Health Check

```
GET /status
GET /health
GET /test
```
Public endpoints, no auth required.

---

## Database

### Schema overview

| Table | Description |
|---|---|
| `tbl_profiles` | User accounts — stores full name, email (unique), hashed password, activation token, and active flag |
| `tbl_categories` | User-defined categories — name, icon, type (`income`/`expense`), linked to a profile |
| `tbl_expense` | Expense records — name, icon, amount, date, linked to a category and profile |
| Income table | Income records — same structure as expenses |

All entities use auto-increment primary keys and carry `createdAt` / `updatedAt` timestamps.

### Development (MySQL)
```
jdbc:mysql://localhost:3306/moneymanager
```
Configured in `Backend/src/main/resources/application.properties`.
JPA DDL is set to `update` (tables are created/altered automatically).

### Production (PostgreSQL)
Configured in `Backend/src/main/resources/application-prod.properties`.
The `prod` profile is activated automatically by the Docker container.

---

## Scheduled Jobs

`NotificationService` runs two background jobs (requires `@EnableScheduling` on the application):

| Job | Schedule | Description |
|---|---|---|
| Daily income/expense reminder | Every day at 10 PM IST (22:00) | Sends an HTML email to all active users prompting them to log their daily transactions |
| Daily expense summary | Every day at 11 PM IST (23:00) | Sends a formatted HTML table of today's expenses to each user who has recorded expenses that day |

Both jobs are powered by `EmailService` (SMTP via Brevo) with `BrevoEmailClient` as an HTTP API fallback.

---

## Frontend Pages

| Route | Component | Description |
|---|---|---|
| `/` | `LandingPage` | Public marketing page with feature highlights and sign-up CTA |
| `/login` | `Login` | Email + password login form |
| `/signup` | `Signup` | Registration form |
| `/dashboard` | `Home` | Summary dashboard with balance, charts, and recent transactions |
| `/expense` | `Expense` | Add and view current month's expenses |
| `/income` | `Income` | Add and view current month's incomes |
| `/category` | `Category` | Create and manage income/expense categories |
| `/filter` | `Filter` | Filter and search transactions by type, date, keyword |

---

## Security Notes

1. **Rotate committed secrets immediately.** Both `application.properties` and `application-prod.properties` contain database credentials and a JWT secret that have been committed to the repository. Rotate all credentials and move them to environment variables or a secrets manager.
2. **CORS is currently wide open** (`allowedOrigins("*")`). Restrict this to your frontend origin(s) in production.
3. **JWT secret** should be a long, random string provided securely — never commit it to VCS.
4. **Spring Actuator** endpoints should be restricted or secured before going to production.
5. Passwords are hashed with **BCrypt** before storage — this is handled correctly.
6. All API routes except `/register`, `/login`, `/activate`, `/status`, and `/test/**` require a valid JWT.

---

## Troubleshooting

| Problem | Solution |
|---|---|
| Backend fails to start | Check that MySQL is running and credentials in `application.properties` are correct. If using the `prod` profile, check PostgreSQL connectivity. |
| `No active profile` error | Either set `spring.profiles.active` or override it with `-Dspring.profiles.active=dev` |
| Emails not sending | Verify `BREVO_USERNAME`, `BREVO_PASSWORD`, `BREVO_MAIL`, and `BREVO_API_KEY` are correctly set. Brevo HTTP API is used as a fallback if SMTP is blocked. |
| Frontend can't reach backend | Ensure the Axios base URL in the frontend matches your backend URL including the `/api/v1.0` context path. Check browser console for CORS errors. |
| Login returns 403 | Account hasn't been activated via email. Check your inbox for the activation link. |
| JWT expired | Token lifetime is 1 hour (`jwt.expiration=3600000`). Log in again to get a fresh token. |

---

## Recommended Next Steps

1. **Move all secrets out of source code** and add a `.env.example` file documenting required environment variables.
2. **Restrict CORS** to the frontend origin in production (`SecurityConfig.java`).
3. **Add Swagger / OpenAPI** documentation (`springdoc-openapi`) for a browsable API explorer.
4. **Add CI/CD pipeline** to build and test the backend, build and lint the frontend, and publish Docker images.
5. **Secure Actuator endpoints** using Spring Security's `requestMatchers`.
6. **Add input validation** (`@Valid` + `@NotBlank`, `@Positive`, etc.) to DTO classes.
7. **Write unit and integration tests** for services and controllers.
