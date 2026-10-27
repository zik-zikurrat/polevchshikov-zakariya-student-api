# Polevchshikov Zakariya — Student API

Spring Boot CRUD application for managing students, built for the DevOps midterm.

**Author:** Polevchshikov Zakariya · Group IT2-2312 · Student ID 37052

**Status:** v1.0 - CRUD API containerized with Docker, built by Jenkins and documented by Polevchshikov Zakariya.

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

## Run with Docker Compose

```bash
cp .env.example .env        # then set a real POSTGRES_PASSWORD
docker compose up -d --build
curl http://localhost:8082/polevchshikov-zakariya/api/students
docker compose down         # data stays in the polevchshikov-zakariya-pgdata volume
```

## Jenkins

Job `Polevchshikov_Zakariya_Student_API` runs the `Jenkinsfile`:
Checkout → Build → Test → Docker Build → Verify.

- **Trigger:** `pollSCM('* * * * *')` — Jenkins checks GitHub every minute and starts a build after a new commit on `main`.
- **Parameter `BRANCH`** (default `main`): *Build with Parameters* → `test/polevchshikov-zakariya-failure`
  builds the test branch used for the failure demo.
- **Credentials:** the database password for the smoke test is stored in Jenkins Credentials
  (`polevchshikov-zakariya-db`) and is masked as `****` in the console output.

## API tests with Postman

Import `postman/Polevchshikov_Zakariya_Student_API.postman_collection.json` into Postman and run the folder,
or from the terminal:

```bash
npx newman run postman/Polevchshikov_Zakariya_Student_API.postman_collection.json
```

## Run tests locally

```bash
./gradlew test
```
