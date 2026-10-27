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
| DELETE | `/api/students/{id}` | Delete a student |

## Run tests locally

```bash
./gradlew test
```
