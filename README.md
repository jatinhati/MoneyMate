# MoneyMate 💰

**MoneyMate** is a full-stack personal finance management web application that helps you track your income, expenses, and spending habits — all in one place. It features a Spring Boot REST API backend and a modern React frontend.
---
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/11349842-2f8b-4ba3-ab45-3a07dd952b1d" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/1a37d2c6-8cb0-427d-9c95-43d0f3dbcbb9" />

## Tech Stack

### Backend

<p align="left">
  <img src="https://img.shields.io/badge/Java-21-yellow.svg?logo=java&logoColor=white" alt="Java" height="28"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.5.5-brightgreen.svg?logo=springboot" alt="Spring Boot" height="28"/>
  <img src="https://img.shields.io/badge/Spring_Security-6.x-green.svg?logo=springsecurity" alt="Spring Security" height="28"/>
  <img src="https://img.shields.io/badge/JPA-ORM-blue.svg?logo=hibernate" alt="JPA" height="28"/>
  <img src="https://img.shields.io/badge/Maven-Build-blueviolet?logo=apachemaven" alt="Maven" height="28"/>
  <img src="https://img.shields.io/badge/MySQL-Dev-4479A1?logo=mysql&logoColor=white" alt="MySQL" height="28"/>
  <img src="https://img.shields.io/badge/PostgreSQL-Prod-4169E1?logo=postgresql&logoColor=white" alt="PostgreSQL" height="28"/>
  <img src="https://img.shields.io/badge/Lombok-Annotate-red?logo=lombok" alt="Lombok" height="28"/>
  <img src="https://img.shields.io/badge/JWT-Auth-orange?logo=jsonwebtokens" alt="JWT" height="28"/>
  <img src="https://img.shields.io/badge/Docker-Container-blue?logo=docker&logoColor=white" alt="Docker" height="28"/>
</p>

### Frontend

<p align="left">
  <img src="https://img.shields.io/badge/React-19-61DAFB?logo=react" alt="React" height="28"/>
  <img src="https://img.shields.io/badge/Vite-7-646CFF?logo=vite&logoColor=yellow" alt="Vite" height="28"/>
  <img src="https://img.shields.io/badge/Tailwind_CSS-4-06B6D4?logo=tailwindcss" alt="Tailwind CSS" height="28"/>
  <img src="https://img.shields.io/badge/React_Router-7-CA4245?logo=reactrouter" alt="React Router" height="28"/>
  <img src="https://img.shields.io/badge/Axios-HTTP-5A29E4?logo=axios&logoColor=white" alt="Axios" height="28"/>
  <img src="https://img.shields.io/badge/Recharts-3-FF7300?logo=chartdotjs&logoColor=white" alt="Recharts" height="28"/>
  <img src="https://img.shields.io/badge/Framer_Motion-12-0055FF?logo=framer" alt="Framer Motion" height="28"/>
  <img src="https://img.shields.io/badge/Lucide_React-Icons-black?logo=lucide&logoColor=white" alt="Lucide React" height="28"/>
  <img src="https://img.shields.io/badge/EmojiPicker-4-FFDD67" alt="Emoji Picker" height="28"/>
  <img src="https://img.shields.io/badge/Moment.js-Dates-eb6d4a?logo=momentjs&logoColor=white" alt="Moment.js" height="28"/>
  <img src="https://img.shields.io/badge/Hot%20Toast-2-EA580C?logo=react&logoColor=white" alt="React Hot Toast" height="28"/>
</p>


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

> ⚠️ **Security Warning:** `application.properties` and `application-prod.properties` currently contain hardcoded credentials. These must be moved to environment variables or a secrets manager and never committed to source code.

---

## API Reference

All endpoints are prefixed with `/api/v1.0`. Authenticated endpoints require the header:
```
Authorization: Bearer <jwt_token>
```
... (the rest of the file remains unchanged)
