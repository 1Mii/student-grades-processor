package system.java_app.controler;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.java_app.model.Student;
import system.java_app.service.student.StudentService;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/students")
@CrossOrigin(origins = "http://localhost:8000")
public class StudentControler {
    private final StudentService studentService;

    @GetMapping()
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity(students, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") Long id) {
        return studentService.getStudentById(id).map(student -> new ResponseEntity<>(student, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/nmec/{nmec}")
    public ResponseEntity<Student> getStudentByNmec(@PathVariable("nmec") Long nmec) {
        return studentService.getStudentByNmec(nmec).map(student -> new ResponseEntity<>(student, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("{id}/mean")
    public ResponseEntity<Double> getStudentMeanById(@PathVariable("id") Long id) {
        return studentService.getStudentMeanById(id).map(mean -> new ResponseEntity<>(mean, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/nmec/{nmec}/mean")
    public ResponseEntity<Double> getStudentMeanByNmec(@PathVariable("nmec") Long nmec) {
        return studentService.getStudentMeanByNmec(nmec).map(mean -> new ResponseEntity<>(mean, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }    
}
