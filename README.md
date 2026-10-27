# Polevchshikov Zakariya — Student API

Spring Boot CRUD application for managing students, built for the DevOps midterm.

**Author:** Polevchshikov Zakariya · Group IT2-2312 · Student ID 37052

**Status:** initial version of the Student API.

## Technologies

Linux · Bash · Git · GitHub · Spring Boot 4 (Java 21, Gradle) · PostgreSQL · Docker · Docker Compose · Jenkins

## Project structure

```
polevchshikov-zakariya-student-api/
├── scripts/            # Polevchshikov_Zakariya_system_report.sh
├── logs/               # personal system report log and log analysis
├── backups/            # README backup
├── src/                # Spring Boot application and tests
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── build.gradle
└── README.md
```

## API

Base URL: `http://localhost:8082/polevchshikov-zakariya`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/` | Application info (student name, version) |
| GET | `/api/students` | List all students |
| GET | `/api/students/{id}` | Get one student |
| POST | `/api/students` | Create a student |
| PUT | `/api/students/{id}` | Update a student |
| DELETE | `/api/students/{id}` | Delete a student |

## Changes by Polevchshikov Zakariya (feature/polevchshikov-zakariya-crud)

- **Update operation:** new `PUT /api/students/{id}` endpoint changes a student's name, email and group.
- **Validation:** `firstName`, `lastName` and `email` are required, `email` must be a valid address;
  invalid requests return `400 Bad Request` with the list of wrong fields.
- **Nonexistent student:** `GET/PUT/DELETE` for an unknown id return `404 Not Found` with a JSON message,
  e.g. `{"status":404,"message":"Student with id 999 not found"}`.

## Run tests locally

```bash
./gradlew test
```
