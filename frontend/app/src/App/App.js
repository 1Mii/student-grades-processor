import { useState, useRef, useEffect } from "react";
import StudentDashboard from "../StudentDashboard/StudentDashboard";
import StudentInfo from "../StudentInfo/StudentInfo";
import UploadFile from "../UploadFile/UploadFile";
import "./App.css";
import SearchStudent from "../SearchStudent/SearchStudent";

function App() {
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [refreshList, setRefreshList] = useState(0);
  const studentInfoRef = useRef();

  const handleUploadSuccess = () => {
    setRefreshList(prev => prev + 1);
  };

  const handleSelectStudent = (student) => {
    setSelectedStudent(student);
    setTimeout(() => {
      studentInfoRef.current?.scrollIntoView({
        behavior: "smooth",
        block: "start"
      });
    }, 100);
  };

  const onSearchSubmit = (searchData) => {    
    setSelectedStudent({[searchData.type]: searchData.value});
    
    setTimeout(() => {
      studentInfoRef.current?.scrollIntoView({
        behavior: "smooth",
        block: "start"
      });
    }, 100);
  };

  return (
    <div className="student-app">
      <h1 className="student-app__title">Student Management</h1>

      <div className="student-app__grid">
        <section className="student-app__panel">
          <UploadFile onUploadSuccess={handleUploadSuccess} />
        </section>
        <section>
          <SearchStudent
            handleSearch={onSearchSubmit}
          />
        </section>
        <section className="student-app__panel" ref={studentInfoRef}>
          <StudentInfo
            id={selectedStudent?.id}
            nmec={selectedStudent?.nmec}
          />
        </section>
        <section className="student-app__panel">
          <StudentDashboard
            refreshList={refreshList}
            onSelectStudent={handleSelectStudent}
          />
        </section>
      </div>
    </div>
  );
}

export default App;