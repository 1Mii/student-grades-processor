# Student Grades Processor

## What this is
This project is a full web app to import student grades from CSV files, store them in PostgreSQL, and browse/search students in a React UI.

It has 3 services:
- `db`: PostgreSQL
- `api`: Spring Boot (Java 17, JPA)
- `frontend`: React app

## Main features
- Upload a `.csv` file through the frontend.
- Validate each CSV row before storing.
- Save valid students and their grades in PostgreSQL.
- Show how many records were valid vs invalid in each upload.
- List all students.
- Search student details by `id` or `nmec`.
- Show student grades and computed mean.

## CSV format expected
Each row must have exactly 5 fields (semicolon `;` separated):

```text
<nmec>;<FirstName LastName>;<grade1>;<grade2>;<grade3>
```

Example:

```text
12345;John Doe;15.5;14;18
```

Validation rules:
- `nmec` must be a number (`Long`).
- Name must be exactly two words with letters only.
- Grades must be numeric and between `0` and `20`.

Invalid rows are skipped and counted as invalid.  
Also, duplicated `nmec` values (already in DB) are rejected by DB uniqueness and count as invalid.

## Run with Docker Compose (recommended)
From project root:

```bash
export API_URL=http://localhost:8080
docker compose up --build
```

Then open:
- Frontend: `http://localhost:8000`
- API: `http://localhost:8080`

Ports used:
- `8000` -> React frontend container (`3000`)
- `8080` -> Spring API
- `5432` -> PostgreSQL (inside compose network)

To stop:

```bash
docker compose down
```

To also remove database persisted data:

```bash
docker compose down -v
```

## Run locally without Docker (optional)
You can run backend and frontend manually, but you still need PostgreSQL running.

### 1) Start PostgreSQL
Create a DB with:
- database: `db_lab1`
- user: `user_lab1`
- password: `password_lab1`

### 2) Start backend

```bash
cd java-app
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=db_lab1
export DB_USER=user_lab1
export DB_PASSWORD=password_lab1
./mvnw spring-boot:run
```

### 3) Start frontend

```bash
cd frontend/app
npm install
PORT=8000 REACT_APP_API_URL=http://localhost:8080 npm start
```

Frontend local URL: `http://localhost:8000`.

## API endpoints used by the frontend
- `POST /api/upload-file` (multipart field name: `file`)
- `GET /api/students`
- `GET /api/students/{id}`
- `GET /api/students/nmec/{nmec}`
- `GET /api/students/{id}/mean`
- `GET /api/students/nmec/{nmec}/mean`

## Expected results in the app
After uploading a valid CSV:
- You should see `Ficheiro enviado com sucesso!`.
- `Valid` counter increases by number of accepted rows.
- `Invalid` counter increases by rejected rows.
- Students table refreshes with newly imported students.

When selecting or searching a student:
- Details panel shows `ID`, `NMEC`, all exams/grades, and final overall grade.
- If student does not exist, UI shows: `The ID/NMEC given does not exist. Please try again.`

## Project structure
```text
.
├── compose.yml
├── java-app/        # Spring Boot API
└── frontend/        # React app
```
