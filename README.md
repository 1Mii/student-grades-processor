# CSV → PostgreSQL ETL — Java CLI (Maven + Docker)

**Short description**
Small CLI Java application (Maven) that validates CSV rows, parses student + grades data and persists it into a normalized PostgreSQL schema (`Students`, `Grades`). Packaged as a fat JAR and provided with a multi-stage Dockerfile and `docker-compose` to bootstrap a local Postgres instance and run the app.
This project was implemented as part of an IES laboratory assignment — at the end of the semester the same course will produce a larger architectureed project (Spring Boot backend, Kafka, databases, React frontend, etc.). This README already assumes the project has been restructured as a Maven project and packaged into an executable jar.

---

## What this project does (features)

* Parses CSV files with rows in the expected format (see *CSV format* below).
* Validates each CSV entry:

  * `num_mec` must be an integer.
  * `full name` must be two words (first + last).
  * grades must be numeric and in range `0.0`–`20.0`.
* Produces counts of valid / invalid records and prints progress to `stdout`.
* Persists validated records into PostgreSQL using:

  * JDBC `PreparedStatement`s.
  * Explicit transactions (`setAutoCommit(false)` + `commit()` / `rollback()` on error).
  * Batched grade inserts for throughput.
  * `INSERT ... ON CONFLICT (num_mec) DO NOTHING` to avoid duplicate student inserts (idempotent import behaviour).
* Packaged as a single "jar-with-dependencies" (fat JAR) for easy deployment.
* Dockerized with a multi-stage `Dockerfile` and `docker-compose.yml` that:

  * Boots a Postgres service (runs `setup.sql` at container init to create schema).
  * Builds the Java app image (Maven build stage) and runs the packaged jar.
  * Mounts a folder for CSV files so you can provide different datasets.

---

## Repo layout (expected, Maven-style)

```
.
├─ pom.xml
├─ Dockerfile
├─ docker-compose.yml
├─ setup.sql
├─ src/
│  ├─ main/
│  │  ├─ java/         (application packages, e.g. system.*)
│  │  └─ resources/    (put CSV files you want to import here)
│  └─ test/
└─ README.md
```

---

## Prerequisites

* Docker & Docker Compose (recommended for local quick start)
* OR Java 17 and Maven if you prefer to run locally without Docker

---

## Quick start — Docker (recommended)

1. Clone the repository.

2. Put your CSV file(s) into `src/main/resources/` (the project `docker-compose.yml` mounts that folder into the container).

3. Build and start services:

```bash
# from project root
docker compose up --build -d
```

* `docker-compose` will:

  * Start a Postgres container and run `setup.sql` (creating `Students` and `Grades` tables).
  * Build the Java app image (multi-stage Maven build -> fat JAR).
  * Start the app container with the packaged jar.

4. Interact with the app (it is a CLI interactive program). With the compose setup you can attach to the running app container or run it interactively:

```bash
# run interactively (recommended for one-off run)
docker compose run --rm app
```

The application will prompt with a menu:

```
1) Process and store data from CSV file
2) Calculate the average of a student based on the student identifier
3) Display all grades associated with a specific student (assuming via id)
```

Follow prompts (e.g. choose `1` and provide the filename that exists under `src/main/resources/`).

---

## Run without Docker (local)

1. Build with Maven:

```bash
mvn clean package
```

2. Run the fat JAR (the artifact name follows the assembly plugin configuration):

```bash
java -jar target/java-app-1.0-SNAPSHOT-jar-with-dependencies.jar
```

3. Ensure the following environment variables are set in your shell (the app reads DB connection info and data path from env vars):

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=db_lab1
export DB_USER=user_lab1
export DB_PASSWORD=password_lab1
export DATA_PATH=/absolute/path/to/src/main/resources/
```

Make sure a PostgreSQL instance is accessible with those credentials and the schema (see `setup.sql`) exists.

---

## CSV format (input expectations)

Each line must follow the semicolon-delimited format (example):

```
<num_mec>;<Full Name (First Last)>;<grade1>;<grade2>;<grade3>
```

* Exactly 5 columns expected per line (the current implementation rejects lines that don't have exactly 5 fields).
* Example valid line:

```
12345;John Doe;15.5;14.0;18.0
```

* The parser:

  * Splits on `;`
  * Validates `num_mec` is integer
  * Validates name matches `^[A-Za-z]+ [A-Za-z]+$`
  * Validates grade values are floats in `0..20`

If a row is invalid it will be skipped and counted as a rejected record; valid rows will be persisted.

---

## Database schema (`setup.sql`)

Tables created at DB initialization:

```sql
CREATE TABLE IF NOT EXISTS Students (
    id SERIAL PRIMARY KEY,
    num_mec INT NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Grades (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES Students(id),
    exam INT NOT NULL,
    grade FLOAT NOT NULL
);
```

---

## Design & implementation notes

* **Idempotent imports:** `ON CONFLICT (num_mec) DO NOTHING` prevents duplicate student rows when re-running imports.
* **Transactions & batch inserts:** Student insert + batched grade inserts are wrapped in a transaction so either all grades for a student are saved or none (rollback on failure).
* **Validation layer:** CSVReader + EntryValidator enforce basic format and semantic checks before persistence.
* **Connection management:** `DatabaseConnection` obtains a single `Connection` using JDBC and environment-driven configuration.
* **CLI operator:** `AppOperator` offers a simple interactive menu to import files and query saved students/grades.
* **Packaging & Docker:** Maven `maven-assembly-plugin` is used to produce a single jar-with-dependencies; Dockerfile is a multi-stage build (Maven build stage + minimal runtime stage) to keep the runtime image small.

---

## Environment variables

The application reads these environment variables:

* `DB_HOST` — database host (e.g. `db` when using docker-compose)
* `DB_PORT` — database port (e.g. `5432`)
* `DB_NAME` — database name (e.g. `db_lab1`)
* `DB_USER` — database user
* `DB_PASSWORD` — database password
* `DATA_PATH` — path inside the container / local filesystem where CSV files are read from (used by `AppOperator`)

Example `.env` for local development:

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=db_lab1
DB_USER=user_lab1
DB_PASSWORD=password_lab1
DATA_PATH=/path/to/repo/src/main/resources/
```

---

## Known limitations & future improvements

> **Important:** this project is a lab assignment; a larger, re-architected version will be produced at the end of the semester (Spring Boot microservice(s), Kafka for eventing/streaming, a relational DB backend with connection pooling, and a React frontend). The README below already assumes the project packaging + Docker improvements described above.

Current limitations and sensible next steps:

* Use a connection pool (e.g. HikariCP) instead of a single raw `Connection`.
* Add structured logging (SLF4J + Logback) instead of `System.out` / `System.err`.
* Replace `System.exit(...)` on library code paths with exceptions / proper error handling.
* Add automated tests (unit tests for parsing/validation; integration tests with a testcontainer Postgres).
* Improve CSV schema flexibility (support variable number of grades) and add a better error reporting format (per-row reason).
* Add non-interactive CLI flags/subcommands (e.g. `--import file.csv`), to make scripting with Docker easier.
* For the final course project: convert to Spring Boot service(s), introduce Kafka for streaming events, secure endpoints, and add a React frontend for operations and monitoring.

---

## A note about origin / course

This project was implemented as part of an **IES** laboratory exercise. The repository contains the lab implementation plus the Maven + Docker improvements requested by the course. At the end of the semester I will produce an extended version of the assignment (Spring Boot backend, Kafka, persistent DBs, React frontend and improved architecture) as part of the same course.

---