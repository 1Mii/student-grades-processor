package system.java_app.model;

import jakarta.persistence.*;
import lombok.*;
import system.java_app.model.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "grades")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "exam", nullable = false)
    private Integer exam;

    @Column(name = "grade", nullable = false)
    private Double grade; 

    public Grade(Student student, Integer exam, Double grade) {
        this.student = student;
        this.exam = exam;
        this.grade = grade;
    }
}
