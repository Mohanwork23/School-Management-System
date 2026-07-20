# School Management System (SMS)

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

Build and run:

```bash
mvn clean package -DskipTests
java -jar target/SMS-0.0.1-SNAPSHOT.jar
```

API Docs available at: `http://localhost:8080/swagger-ui.html`

---

## CI/CD

GitHub Actions workflow triggers on every push to `main`:
- Builds the project with Maven
- Packages the application as a JAR

See `.github/workflows/maven-ci.yml`

---

## Development Log

| Day | Date | Feature Added |
|-----|------|---------------|
| Day 1 | 2026-07-19 | Project setup, Spring Boot init, MySQL config, JWT security, Role-based auth |
| Day 2 | 2026-07-20 | Student & Teacher management APIs, Document upload, CI/CD pipeline setup |
| Day 3 | 2026-07-21 | Attendance system, Fee management, Fee structure & payment APIs |
| Day 4 | 2026-07-22 | Timetable management, Exam creation, Result entry & grade reports |
| Day 5 | 2026-07-23 | Assignment management, Submission tracking, Parent portal APIs |
| Day 6 | 2026-07-24 | Email & OTP service, Notification system, Global exception handling |
| Day 7 | 2026-07-25 | API documentation (Swagger), Code refactoring, Performance improvements |

---

## Resume Highlights

- Designed and developed a **monolithic Spring Boot REST API** serving 5+ user roles
- Implemented **JWT-based stateless authentication** with role-level endpoint security
- Built **fee management module** with dynamic fee structures, components, and payment tracking
- Developed **attendance tracking system** with per-class daily marking and reporting
- Integrated **JavaMail SMTP** for OTP verification and automated email notifications
- Configured **GitHub Actions CI/CD** pipeline for automated build and packaging
- Applied **layered architecture** (Controller → Service → Repository) with clean separation of concerns
