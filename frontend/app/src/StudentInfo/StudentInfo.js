import { useEffect, useState } from "react";
import "./StudentInfo.css";

function StudentInfo({ id, nmec }) {
  const [student, setStudent] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!id && !nmec) {
      setStudent(null);
      return;
    }

    const fetchStudent = async () => {
      setLoading(true);
      setError(null);
      try {
        const url = id
          ? `${process.env.REACT_APP_API_URL}/api/students/${id}`
          : `${process.env.REACT_APP_API_URL}/api/students/nmec/${nmec}`;

        const response = await fetch(url);

        if (!response.ok) {
          throw new Error("Student not found or server error");
        }

        const data = await response.json();
        setStudent(data);
      } catch (error) {
        console.error("Error:", error);
        setStudent(null);
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchStudent();
  }, [id, nmec]);

  if (!id && !nmec) return <p className="info-empty">Select a student in the list or search for one</p>;
  if (loading) return <p className="info-empty">Loading...</p>;
  if (error) return (
    <div className="info-error">
      <p className="info-error-hint">The ID/NMEC given does not exist. Please try again.</p>
    </div>
  );
  if (!student) return null;

  return (
    <div>
      <h2 className="info-card__title">
        Details: {student.first_name} {student.last_name}
      </h2>

      <div className="info-meta">
        <p className="info-meta__item">ID: {student.id}</p>
        <p className="info-meta__item">NMEC: {student.nmec}</p>
      </div>

      <div className="info-grades">
        <h3 className="info-grades__title">Grades</h3>

        <ul className="info-grade-list">
          {student.grades?.map(grade => (
            <li key={grade.id} className="info-grade-list__item">
              Exam: {grade.exam} | Grade: <strong>{grade.grade}</strong>
            </li>
          ))}
        </ul>

        <p className="info-total">
          Final overall grade: {student.gradesMean}
        </p>
      </div>
    </div>
  );
}

export default StudentInfo;