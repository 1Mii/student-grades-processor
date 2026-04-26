package system.java_app.service.student;

import java.util.List;
import java.util.Optional;

import system.java_app.model.Student;

public interface StudentService {
    public Optional<Student> getStudentById(Long id);
    public Optional<Student> getStudentByNmec(Long nmec);
    public Optional<Double> getStudentMeanByNmec(Long nmec);
    public Optional<Double> getStudentMeanById(Long id);
    public List<Student> getAllStudents();
}
