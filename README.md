# School Management System (SMS)

[![CI](https://github.com/Mohanwork23/School-Management-System/actions/workflows/maven-ci.yml/badge.svg?branch=main)](https://github.com/Mohanwork23/School-Management-System/actions/workflows/maven-ci.yml)
[![CI (daily)](https://github.com/Mohanwork23/School-Management-System/actions/workflows/maven-ci.yml/badge.svg?branch=daily)](https://github.com/Mohanwork23/School-Management-System/actions/workflows/maven-ci.yml)

[![Container Publish](https://github.com/Mohanwork23/School-Management-System/actions/workflows/docker-publish.yml/badge.svg?branch=main)](https://github.com/Mohanwork23/School-Management-System/actions/workflows/docker-publish.yml)
[![Security Scan](https://github.com/Mohanwork23/School-Management-System/actions/workflows/scan-image.yml/badge.svg?branch=main)](https://github.com/Mohanwork23/School-Management-System/actions/workflows/scan-image.yml)

A production-ready, full-stack **School Management System** built with **Spring Boot 3**, designed to digitize and streamline school operations including student enrollment, attendance, fee management, timetables, exams, and parent communication.

> Developed as part of 3 years of hands-on Java backend development experience, demonstrating real-world application of Spring Boot, REST APIs, JWT Security, JPA/Hibernate, and MySQL.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.5.3 |
| Security | Spring Security, JWT (jjwt 0.11.5) |
| Database | MySQL, Spring Data JPA, Hibernate |
| Email | Spring Mail (SMTP) |
| API Docs | Springdoc OpenAPI / Swagger UI |
| Build | Maven |
| CI/CD | GitHub Actions |
| Utilities | Lombok, DevTools |

---

## Features

- **Role-Based Access Control** — Admin, Teacher, Student, Parent roles with JWT authentication
- **Student Management** — Register, update, view students with document uploads
- **Teacher Management** — Assign subjects, manage teacher profiles and dashboards
- **Attendance System** — Mark and track daily attendance per class
- **Fee Management** — Fee structures, components, payments, and invoice generation
- **Timetable Management** — Create and manage class timetables with slot-based scheduling
- **Exam & Results** — Create exams, enter marks, generate results and grade reports
- **Assignment Management** — Create assignments, track submissions
- **Parent Portal** — Parents can view their child's attendance, results, and fee status
- **Email & OTP Service** — Email notifications and OTP-based verification
- **Document Management** — Upload and download student/teacher documents
- **Global Exception Handling** — Centralized error responses across all APIs

---

## Project Structure

```
src/main/java/com/
├── controller/        # REST API endpoints (Admin, Student, Teacher, Parent, Fee, Timetable)
├── service/           # Business logic interfaces and implementations
├── entity/            # JPA entities (academic, attendance, fees, users, enums)
├── repository/        # Spring Data JPA repositories
├── dto/               # Data Transfer Objects for request/response
├── security/          # JWT filter, SecurityConfig, UserDetailsService
├── email/             # Email and OTP services
├── config/            # OpenAPI config, file download, auth response
├── exception/         # Global exception handler
└── util/              # ID generator utility
```

---

## Getting Started

Set environment variables:

```bash
DB_USERNAME=<your_db_username>
DB_PASSWORD=<your_db_password>
JWT_SECRET=<strong_random_secret_min_32_chars>
MAIL_USERNAME=<your_email>
MAIL_PASSWORD=<your_email_password>
```

The default `dev` profile is intended for local work. For production, set
`SPRING_PROFILES_ACTIVE=prod` with `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`,
`JWT_SECRET`, and `CORS_ALLOWED_ORIGINS`.

Build and run:

```bash
mvn clean package -DskipTests
java -jar target/SMS-0.0.1-SNAPSHOT.jar
```

API Docs available at: `http://localhost:8080/swagger-ui.html`

Health check endpoint available at: `http://localhost:8080/actuator/health`

Status endpoint available at: `http://localhost:8080/api/status` (returns basic app info, timestamp, version, and commit when available)
Readiness endpoint available at: `http://localhost:8080/api/readiness` (checks DB connectivity)

Docker (quick):

```bash
docker build -t sms:daily .
docker run -e DB_USERNAME=root -e DB_PASSWORD=root -p 8080:8080 sms:daily
```

Docker Compose (recommended for local demo):

```bash
docker compose up --build
```

Resume-ready summary:

- Role: Backend engineer — designed and implemented a monolithic Spring Boot REST API for a School Management System handling RBAC, JWT security, attendance, fees, timetables, exams, and parent portal features.
- Highlights: JWT-based stateless auth, MySQL + JPA/Hibernate, OpenAPI docs, email OTP flows, file uploads, and CI via GitHub Actions. See `pom.xml` and `src/main/java/com/SmsApplication.java`.

---

## CI/CD

GitHub Actions workflows run on every push to `main` and `daily`:
- `maven-ci.yml` runs tests and packages the application
- `docker-publish.yml` builds and publishes the app image to GHCR
- `scan-image.yml` scans the published container image for vulnerabilities

See `.github/workflows/maven-ci.yml`, `.github/workflows/docker-publish.yml`, and `.github/workflows/scan-image.yml`

---

## Deployment

### Publish to GitHub Container Registry

1. Ensure `GITHUB_TOKEN` is available in GitHub Actions.
2. Push to `main` or `daily`.
3. The workflow builds and pushes images to:
   - `ghcr.io/<owner>/school-management-system:latest`
   - `ghcr.io/<owner>/school-management-system:<commit-sha>`

### Run locally with Docker Compose

```bash
docker compose up --build
```

This starts:
- MySQL at `localhost:3306`
- the app at `http://localhost:8080`

### Run the container image locally

```bash
docker run -e DB_URL=jdbc:mysql://host.docker.internal:3306/sms \
  -e DB_USERNAME=smsuser \
  -e DB_PASSWORD=smspass \
  -e JWT_SECRET=replace-with-a-long-secret \
  -p 8080:8080 \
  ghcr.io/<owner>/school-management-system:latest
```

---

## Development Log

| Day | Date | Feature Added |
|-----|------|---------------|
| Day 1 | 2026-07-19 | Project setup, Spring Boot init, MySQL config, JWT security, Role-based auth |
| Day 2 | 2026-07-20 | Student & Teacher management APIs, Document upload, CI/CD pipeline setup |
| Day 3 | 2026-07-21 | Enabled Student fee & timetable APIs, Parent fee status API, Notification entity added |
| Day 4 | 2026-07-22 | Notification entity, service & controller added; Student dashboard, fee status & timetable implemented; Parent child fee status implemented |
| Day 5 | 2026-07-23 | Notification triggers on attendance, result & fee payment; SecurityConfig updated for notifications endpoint |
| Day 6 | 2026-07-24 | Input validation with @Valid on DTOs, improved GlobalExceptionHandler with field-level validation errors |
| Day 7 | 2026-07-25 | Swagger @Tag & @Operation on all controllers, JWT Bearer auth in Swagger UI, OpenAPI enhanced with contact info |
| Day 8 | 2026-07-26 | Password change API, OTP-based forgot/reset password flow, fixed TimeTableController null pointer bug, fixed student timetable to return actual entries |
| Day 9 | 2026-07-27 | Admin attendance summary report per class, fee collection report, student search by name/class/status |
| Day 10 | 2026-07-28 | Exam schedule by class, result summary per term with pass/fail stats, teacher search by name/department |
| Day 11 | 2026-07-29 | Parent child timetable & upcoming assignments APIs, admin class-wise student count report |
| Day 12 | 2026-07-30 | Student upcoming exams, teacher assignment submission tracker, admin subject-wise result analysis |
| Day 13 | 2026-07-31 | Admin delete student/teacher, update class, student profile view API |
| Day 14 | 2026-08-01 | Teacher profile API, student self-service password change, enhanced admin dashboard with fees & exams count |
| Day 15 | 2026-08-02 | Admin parent/subject deletion APIs and teacher assignment update API |
| Day 16 | 2026-08-03 | Hardened authentication: verified passwords at login, authenticated password changes, JWT secret validation and restricted CORS |
| Day 17 | 2026-08-04 | Enforced authenticated ownership for student, parent, teacher and notification portal APIs |
| Day 18 | 2026-08-05 | Added JWT and ownership unit tests; separated development and production configuration |
| Day 19 | 2026-08-06 | Hardened document uploads with size, MIME-type and filename validation |

---

## Resume Highlights

- Designed and developed a **monolithic Spring Boot REST API** serving 5+ user roles
- Implemented **JWT-based stateless authentication** with role-level endpoint security
- Built **fee management module** with dynamic fee structures, components, and payment tracking
- Developed **attendance tracking system** with per-class daily marking and reporting
- Integrated **JavaMail SMTP** for OTP verification and automated email notifications
- Configured **GitHub Actions CI/CD** pipeline for automated build and packaging
- Applied **layered architecture** (Controller → Service → Repository) with clean separation of concerns
