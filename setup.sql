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