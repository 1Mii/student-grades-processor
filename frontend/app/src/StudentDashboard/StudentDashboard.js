import { useEffect, useState } from "react";
import "./StudentDashboard.css";

function StudentDashboard({ refreshList, onSelectStudent }) {
  const [students, setStudents] = useState([]);

  useEffect(() => {
    fetch(`${process.env.REACT_APP_API_URL}/api/students`)
      .then(response => response.json())
      .then(data => setStudents(data))
      .catch(error => console.error(error));
  }, [refreshList]);

  return (
    <div>
      <h2 className="dashboard-card__title">Students list</h2>

      <table className="dashboard-table">
        <thead>
          <tr>
            <th>Id</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Nmec</th>
          </tr>
        </thead>

        <tbody>
          {students.length === 0 ? (
            <tr>
              <td className="dashboard-table__empty" colSpan="4">
                No students found.
              </td>
            </tr>
          ) : (
            students.map(student => (
              <tr
                key={student.id}
                className="dashboard-table__row"
                onClick={() => onSelectStudent(student)}
              >
                <td>{student.id}</td>
                <td>{student.first_name}</td>
                <td>{student.last_name}</td>
                <td>{student.nmec}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default StudentDashboard;