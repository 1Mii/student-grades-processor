package system.java_app.service.student;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import system.java_app.model.Student;
import system.java_app.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentServiceImp implements StudentService {
    
    private final StudentRepository repo;

    @Override
    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Student> getStudentByNmec(Long nmec) {
        return repo.findByNmec(nmec);
    }

    @Override
    public Optional<Double> getStudentMeanByNmec(Long nmec) {
        return getStudentByNmec(nmec).map(Student::getGradesMean);
    }

    @Override
    public Optional<Double> getStudentMeanById(Long id) {
        return getStudentById(id).map(Student::getGradesMean);
    }
}
