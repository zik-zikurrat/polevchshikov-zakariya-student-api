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

## Explanations (Polevchshikov Zakariya)

### `>` vs `>>` in Linux (Task 1.4)

Both redirect a command's output into a file and create the file if it does not exist.
`>` **overwrites** the file (its old content is lost), `>>` **appends** to the end of the file.
In this project the log analysis uses `>` (a fresh result every time):
`grep -n Polevchshikov logs/Polevchshikov_Zakariya_system_report.log > logs/Polevchshikov_Zakariya_log_analysis.txt`,
while the system report script uses `tee -a`, which works like `>>`, so every run is added to the personal log.

### Why `.env` files, passwords and database credentials must not be committed (Task 2.4)

- Everything that is committed stays in the Git history forever, even after the file is deleted
  (`git log -p` still shows it), and the repository is public — anyone, including bots that scan GitHub
  for leaked keys, can read it.
- A leaked database password gives direct access to the data; the only fix is to rotate the secret.
- Different environments (laptop, Jenkins, server) need different values, so secrets belong to the
  environment, not to the code.

This project therefore keeps the real values only in `.env` (listed in `.gitignore`), commits `.env.example`
with placeholders, passes the values to the containers as environment variables in `docker-compose.yml`,
and stores the CI database password in Jenkins Credentials (`polevchshikov-zakariya-db`), masked as `****` in the log.

### Controlled pipeline failure (Task 4.3)

On the branch `test/polevchshikov-zakariya-failure` the test `rootShowsStudentName()` was changed to expect
`"Asan Abai"` while `GET /` returns `"Polevchshikov Zakariya"` (commit `6be2a4d`).
In Jenkins build **#2** `./gradlew test` failed with an `AssertionError` (8 tests, 1 failed) and exited with a
non-zero code, so the **Test** stage turned red, **Docker Build** and **Verify** were skipped and the build finished
with `FAILURE` — a broken commit never produced an image. The fix (commit `3d5b4a8`) restored the expected value,
and build **#3** of the same branch passed all 8 tests and finished with `SUCCESS`. The `main` branch was never touched.

### Continuous Integration vs Continuous Delivery vs Continuous Deployment (Task 4.4)

- **Continuous Integration (CI)** — every commit is automatically built and tested so that problems are found
  minutes after they are introduced. Here: Jenkins polls GitHub every minute and runs
  Checkout → Build → Test → Docker Build → Verify.
- **Continuous Delivery** — in addition, every change that passes the pipeline produces a release-ready artifact
  (here: the verified Docker image `polevchshikov-zakariya-student-api:<version tag>`); deploying it to production
  is one manual approval.
- **Continuous Deployment** — every change that passes the pipeline is deployed to production automatically,
  with no human step. This project implements CI (and produces deliverable images); automatic deployment is not
  enabled, as required by the task.

